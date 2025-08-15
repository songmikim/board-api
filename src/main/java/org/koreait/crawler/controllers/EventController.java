package org.koreait.crawler.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawledData;
import org.koreait.crawler.services.CrawledDataInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@Tag(name="환경 행사 API")
public class EventController {

    private final CrawledDataInfoService infoService;

    @GetMapping
    public List<CrawledData> list() {
        return infoService.getList();
    }

    @GetMapping("/{hash}")
    public CrawledData info(@PathVariable("hash") Integer hash) {
        return infoService.get(hash);
    }
}
