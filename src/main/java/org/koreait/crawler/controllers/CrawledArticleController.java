package org.koreait.crawler.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawledArticle;
import org.koreait.crawler.repositories.CrawledArticleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crawler/articles")
@RequiredArgsConstructor
public class CrawledArticleController {
    private final CrawledArticleRepository repository;

    @GetMapping
    public List<CrawledArticle> list(@RequestParam(required = false) String site) { // site 파라미터가 없을 수도 있도록 required=false 설정
        if (site != null && !site.isBlank()) { // site 값이 null이 아니고 공백이 아닐 경우
            return repository.findBySiteNameOrderByPublishedAtDesc(site); // 해당 사이트 이름으로 검색 후 최신 순 정렬
        }
        return repository.findAll(); // site 값이 없으면 전체 기사 목록 반환
    }

    @GetMapping("/{id}")  // /{id} 경로의 GET 요청 처리
    public CrawledArticle detail(@PathVariable Long id) {
        // URL 경로 변수 id를 받아 상세 조회 , id로 조회, 없으면 예외 발생
        return repository.findById(id).orElseThrow();
    }
}
