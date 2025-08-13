package org.koreait.crawler.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.koreait.global.entities.BaseEntity;

import java.time.LocalDate;

@Data // Lombok: Getter, Setter, toString, equals, hashCode 자동 생성
@Entity // JPA 엔티티 클래스임을 명시 → DB 테이블과 매핑됨
public class CrawledArticle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 (DB의 Auto Increment 사용)
    private Long id;

    @Column(length = 100, nullable = false) // null 불가, 반드시 값이 있어야 함
    private String siteName;

    @Column(nullable = false)
    private String title;

    private String link;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedAt;

    @Lob
    private String content;
}
