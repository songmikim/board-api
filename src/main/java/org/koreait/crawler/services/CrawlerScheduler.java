package org.koreait.crawler.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlerScheduler {
    private final CrawlerService crawlerService;

    // cron 표현식: 매 정시(매 시각 0분 0초)마다 실행
    // 형식: 초 분 시 일 월 요일 (여기선 초=0, 분=0 → 매 시각 0분 0초 실행)
    @Scheduled(cron = "0 0 * * * *")
    public void schedule() {
        crawlerService.runEnabled();
    }  // 활성화된 모든 크롤러 설정을 실행
}
