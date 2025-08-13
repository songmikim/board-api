package org.koreait.crawler.repositories;

import org.koreait.crawler.entities.CrawlerConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawlerConfigRepository extends JpaRepository<CrawlerConfig, Long> {
    List<CrawlerConfig> findByEnabledTrue();
}
