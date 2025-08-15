package org.koreait.crawler.services;

import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawlerSetting;
import org.koreait.crawler.repositories.CrawlerSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrawlerSettingService {
    private final CrawlerSettingRepository repository;

    /**
     * 스케줄러 활성화 여부를 조회
     * 설정 행( id=1 )이 아직 생성되지 않았다면 기본값(false: 비활성화)
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isSchedulerEnabled() {
        return repository.findById(1L).map(CrawlerSetting::isScheduler).orElse(false);
    }

    /**
     * 스케줄러 활성화 여부를 저장
     * id=1 행을 조회해 있으면 업데이트, 없으면 새로 생성
     * @param enabled
     */
    @Transactional
    public void setScheduler(boolean enabled) {
        CrawlerSetting setting = repository.findById(1L).orElseGet(CrawlerSetting::new);
        setting.setId(1L);
        setting.setScheduler(enabled);
        repository.saveAndFlush(setting);
    }
}
