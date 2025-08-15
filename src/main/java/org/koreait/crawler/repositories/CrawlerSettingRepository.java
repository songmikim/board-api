package org.koreait.crawler.repositories;

import org.koreait.crawler.entities.CrawlerSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlerSettingRepository extends JpaRepository<CrawlerSetting, Long> {
}
