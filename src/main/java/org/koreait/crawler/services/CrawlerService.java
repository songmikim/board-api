package org.koreait.crawler.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawledArticle;
import org.koreait.crawler.entities.CrawlerConfig;
import org.koreait.crawler.repositories.CrawledArticleRepository;
import org.koreait.crawler.repositories.CrawlerConfigRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CrawlerService {
    private final CrawlerConfigRepository configRepository;
    private final CrawledArticleRepository articleRepository;
    private final ObjectMapper objectMapper;

    public void run(Long configId) {
        // 지정한 configId의 크롤러 실행 메서드, ID로 크롤러 설정 조회, 없으면 예외 발생
        CrawlerConfig config = configRepository.findById(configId).orElseThrow();

        try {
            // 임시 Python 스크립트 파일 생성
            File scriptFile = File.createTempFile("crawler_", ".py");
            // DB에 저장된 스크립트 내용을 파일에 UTF-8로 기록
            Files.writeString(scriptFile.toPath(), config.getScript(), StandardCharsets.UTF_8);

            Process process = new ProcessBuilder("python", scriptFile.getAbsolutePath())
                    .redirectErrorStream(true) // 표준 오류를 표준 출력으로 합침
                    .start(); // Python 프로세스를 실행

            String json;
            try (InputStream in = process.getInputStream()) { // 프로세스의 출력 스트림 읽기
                json = new String(in.readAllBytes(), StandardCharsets.UTF_8); // UTF-8로 변환하여 JSON 문자열로 저장
            }
            process.waitFor(); // 프로세스 종료까지 대기

            // JSON 문자열을 List<Map<String,String>> 형태로 변환
            List<Map<String, String>> items = objectMapper.readValue(json, new TypeReference<>() {});
            for (Map<String, String> item : items) {
                // 새 기사 엔티티 생성
                CrawledArticle article = new CrawledArticle();
                // 사이트명 설정
                article.setSiteName(config.getSiteName());

                article.setTitle(item.getOrDefault("title", ""));
                article.setLink(item.get("link"));
                String dateStr = item.get("date");

                if (dateStr != null && !dateStr.isBlank()) {
                    article.setPublishedAt(LocalDate.parse(dateStr)); // 문자열을 LocalDate로 변환 후 저장
                }
                article.setContent(item.get("content")); // 기사 본문 내용 설정
                articleRepository.save(article); // DB 저장
            }

            scriptFile.delete(); // 임시 파일 삭제
        } catch (Exception e) {
            // ignore errors for now
        }
    }

    public void runEnabled() {
        // 활성화된(enabled=true) 크롤러 설정 모두 실행
        List<CrawlerConfig> configs = configRepository.findByEnabledTrue();
        for (CrawlerConfig config : configs) {
            run(config.getId());
        }
    }
}
