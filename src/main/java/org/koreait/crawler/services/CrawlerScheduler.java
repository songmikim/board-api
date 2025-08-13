package org.koreait.crawler.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlerScheduler {
    private final CrawlerService crawlerService;

    @Scheduled(cron = "0 0 * * * *")
    public void schedule() {
        crawlerService.runEnabled();
    }
}
