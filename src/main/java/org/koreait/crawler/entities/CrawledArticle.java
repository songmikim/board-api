package org.koreait.crawler.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.koreait.global.entities.BaseEntity;

import java.time.LocalDate;

@Data
@Entity
public class CrawledArticle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String siteName;

    @Column(nullable = false)
    private String title;

    private String link;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedAt;

    @Lob
    private String content;
}
