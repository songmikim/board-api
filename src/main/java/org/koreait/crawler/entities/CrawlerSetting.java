package org.koreait.crawler.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.koreait.global.entities.BaseEntity;

@Data
@Entity
public class CrawlerSetting extends BaseEntity {
    @Id
    private Long id = 1L;
    private boolean scheduler;
}
