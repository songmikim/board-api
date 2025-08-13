package org.koreait.crawler.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawlerConfig;
import org.koreait.crawler.repositories.CrawlerConfigRepository;
import org.koreait.crawler.services.CrawlerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crawler/config")
@RequiredArgsConstructor
public class CrawlerAdminController {
    private final CrawlerConfigRepository repository;
    private final CrawlerService crawlerService;

    @GetMapping
    public List<CrawlerConfig> list() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CrawlerConfig save(@RequestBody CrawlerConfig config) {
        return repository.save(config);
    }

    @PatchMapping("/{id}/toggle")
    public CrawlerConfig toggle(@PathVariable Long id) {
        CrawlerConfig config = repository.findById(id).orElseThrow();
        config.setEnabled(!config.isEnabled());
        return repository.save(config);
    }

    @PostMapping("/{id}/run")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void run(@PathVariable Long id) {
        crawlerService.run(id);
    }
}
