package org.koreait.crawler.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawlerConfig;
import org.koreait.crawler.repositories.CrawlerConfigRepository;
import org.koreait.crawler.services.CrawlerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API 요청을 처리하는 컨트롤러 클래스임을 명시
@RequestMapping("/api/v1/crawler/config")
@RequiredArgsConstructor // final 필드 초기화를 위한 생성자 자동 생성
public class CrawlerAdminController {
    private final CrawlerConfigRepository repository;
    private final CrawlerService crawlerService;

    @GetMapping
    public List<CrawlerConfig> list() {
        return repository.findAll();
    } // DB에서 전체 설정 리스트 반환

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // POST 요청: 새 설정 저장, 상태 코드 201 반환
    public CrawlerConfig save(@RequestBody CrawlerConfig config) {
        // 요청 바디(JSON)를 CrawlerConfig 객체로 변환하여 저장
        return repository.save(config);
    }

    @PatchMapping("/{id}/toggle") // PATCH 요청: 특정 크롤러의 활성화 여부 토글
    public CrawlerConfig toggle(@PathVariable Long id) {
        // ID로 설정 조회, 없으면 예외
        CrawlerConfig config = repository.findById(id).orElseThrow();

        // 활성화 상태 반전
        config.setEnabled(!config.isEnabled());

        // 변경된 상태 저장 후 반환
        return repository.save(config);
    }

    @PostMapping("/{id}/run")
    @ResponseStatus(HttpStatus.NO_CONTENT) // POST 요청: 특정 크롤러 즉시 실행, 응답 바디 없음
    public void run(@PathVariable Long id) {
        // 해당 ID 크롤러 실행
        crawlerService.run(id);
    }
}
