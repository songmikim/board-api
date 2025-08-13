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
    public List<CrawledArticle> list(@RequestParam(required = false) String site) {
        if (site != null && !site.isBlank()) {
            return repository.findBySiteNameOrderByPublishedAtDesc(site);
        }
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public CrawledArticle detail(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }
}
