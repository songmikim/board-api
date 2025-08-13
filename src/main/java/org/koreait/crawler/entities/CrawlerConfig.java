package org.koreait.crawler.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.global.entities.BaseEntity;

@Data
@Entity
public class CrawlerConfig extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 (Auto Increment 방식)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String siteName;

    @Lob
    @Column(nullable = false)
    private String script;

    private boolean enabled;
}
