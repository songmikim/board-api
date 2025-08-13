package org.koreait.crawler.repositories;

import org.koreait.crawler.entities.CrawledArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// CrawledArticle 엔티티를 관리하는 JPA 리포지토리
// 첫 번째 제네릭: 엔티티 타입, 두 번째 제네릭: PK 타입(Long)
public interface CrawledArticleRepository extends JpaRepository<CrawledArticle, Long> {
    // 사이트 이름으로 기사 목록을 검색하고 발행일시 기준 내림차순 정렬
    List<CrawledArticle> findBySiteNameOrderByPublishedAtDesc(String siteName);
}
