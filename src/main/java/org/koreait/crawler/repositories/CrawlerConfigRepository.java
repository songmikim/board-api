package org.koreait.crawler.repositories;

import org.koreait.crawler.entities.CrawlerConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// CrawlerConfig 엔티티를 관리하는 JPA 리포지토리
// 첫 번째 제네릭: 엔티티 타입(CrawlerConfig)
// 두 번째 제네릭: PK 타입(Long)
public interface CrawlerConfigRepository extends JpaRepository<CrawlerConfig, Long> {
    List<CrawlerConfig> findByEnabledTrue();
}
