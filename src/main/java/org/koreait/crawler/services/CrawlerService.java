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
        CrawlerConfig config = configRepository.findById(configId).orElseThrow();

        try {
            File scriptFile = File.createTempFile("crawler_", ".py");
            Files.writeString(scriptFile.toPath(), config.getScript(), StandardCharsets.UTF_8);

            Process process = new ProcessBuilder("python", scriptFile.getAbsolutePath())
                    .redirectErrorStream(true)
                    .start();

            String json;
            try (InputStream in = process.getInputStream()) {
                json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            process.waitFor();

            List<Map<String, String>> items = objectMapper.readValue(json, new TypeReference<>() {});
            for (Map<String, String> item : items) {
                CrawledArticle article = new CrawledArticle();
                article.setSiteName(config.getSiteName());
                article.setTitle(item.getOrDefault("title", ""));
                article.setLink(item.get("link"));
                String dateStr = item.get("date");
                if (dateStr != null && !dateStr.isBlank()) {
                    article.setPublishedAt(LocalDate.parse(dateStr));
                }
                article.setContent(item.get("content"));
                articleRepository.save(article);
            }

            scriptFile.delete();
        } catch (Exception e) {
            // ignore errors for now
        }
    }

    public void runEnabled() {
        List<CrawlerConfig> configs = configRepository.findByEnabledTrue();
        for (CrawlerConfig config : configs) {
            run(config.getId());
        }
    }
}
