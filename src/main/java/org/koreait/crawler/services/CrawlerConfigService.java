package org.koreait.crawler.services;

import lombok.RequiredArgsConstructor;
import org.koreait.crawler.controllers.RequestCrawling;
import org.koreait.crawler.entities.CrawlerConfig;
import org.koreait.crawler.repositories.CrawlerConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlerConfigService {
    private final CrawlerConfigRepository repository;

    /**
     * 전달받은 크롤링 설정 폼 목록을 엔티티로 변환하고,
     * 기존 설정을 모두 삭제한 뒤 새 목록을 일괄 저장합니다.
     * @param forms
     */
    public void save(List<RequestCrawling> forms) {
        List<CrawlerConfig> items = forms.stream().map(form -> {
            CrawlerConfig config = new CrawlerConfig();
            config.setUrl(form.getUrl());
            // 키워드: List<String> → "키워드1\n키워드2\n..." 형태로 직렬화
            config.setKeywords(form.getKeywords() == null ? null : String.join("\n", form.getKeywords()));
            config.setLinkSelector(form.getLinkSelector());
            config.setTitleSelector(form.getTitleSelector());
            config.setDateSelector(form.getDateSelector());
            config.setContentSelector(form.getContentSelector());
            config.setUrlPrefix(form.getUrlPrefix());
            return config;
        }).toList();

        repository.deleteAll();
        repository.saveAllAndFlush(items);
    }

    /**
     * 저장된 모든 크롤러 설정을 조회
     * @return
     */
    public List<CrawlerConfig> gets() {
        return repository.findAll();
    }
}
