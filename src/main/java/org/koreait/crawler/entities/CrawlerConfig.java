package org.koreait.crawler.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.crawler.controllers.RequestCrawling;
import org.koreait.global.entities.BaseEntity;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Data
@Entity
public class CrawlerConfig extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String url;

    @Lob
    private String keywords;

    private String linkSelector;
    private String titleSelector;
    private String dateSelector;
    private String contentSelector;
    private String urlPrefix;

    public List<String> getKeywordList() {
        if(!StringUtils.hasText(keywords)) {
            return List.of();
        }
        return Arrays.stream(keywords.replace("\r", "").split("\n"))
                .filter(StringUtils::hasText)
                .toList();
    }

    public RequestCrawling toRequest() {
        RequestCrawling form = new RequestCrawling();
        form.setUrl(url);
        form.setKeywords(getKeywordList());
        form.setLinkSelector(linkSelector);
        form.setTitleSelector(titleSelector);
        form.setDateSelector(dateSelector);
        form.setContentSelector(contentSelector);
        form.setUrl(urlPrefix);
        return form;
    }

}
