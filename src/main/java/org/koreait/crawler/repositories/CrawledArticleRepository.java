package org.koreait.crawler.repositories;

import org.koreait.crawler.entities.CrawledArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawledArticleRepository extends JpaRepository<CrawledArticle, Long> {
    List<CrawledArticle> findBySiteNameOrderByPublishedAtDesc(String siteName);
}
