package org.koreait.crawler.services;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawledData;
import org.koreait.crawler.entities.QCrawledData;
import org.koreait.crawler.repositories.CrawledDataRepository;
import org.koreait.global.exceptions.NotFoundException;
import org.koreait.global.search.CommonSearch;
import org.koreait.global.search.ListData;
import org.koreait.global.search.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CrawledDataInfoService {
    private final CrawledDataRepository repository;
    private final HttpServletRequest request;

    /**
     * 크롤링된 데이터 목록을 페이지 단위로 조회
     * @param search 공통 검색 및 페이징 정보
     * @return 목록 데이터와 페이징 정보
     */
    public ListData<CrawledData> getList(CommonSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = 10; // 한 페이지당 10개 고정
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "date"));

        /* 검색 조건 처리 S*/
        String sopt = search.getSopt();
        String skey = search.getSkey();
        LocalDate sDate = search.getSDate();
        LocalDate eDate = search.getEDate();

        BooleanBuilder andBuilder = new BooleanBuilder();
        QCrawledData crawledData = QCrawledData.crawledData;

        if (sDate != null) {
            andBuilder.and(crawledData.date.goe(sDate));
        }

        if (eDate != null) {
            andBuilder.and(crawledData.date.loe(eDate));
        }

        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            StringExpression title = crawledData.title;
            StringExpression content = crawledData.content;
            StringExpression fields;

            if (sopt.equals("TITLE")) {
                fields = title;
            } else if (sopt.equals("CONTENT")) {
                fields = content;
            } else {
                fields  = title.concat(content);
            }

            andBuilder.and(fields.contains(skey));
        }
        /* 검색 조건 처리 E*/

        Page<CrawledData> result = repository.findAll(andBuilder, pageable);
        int total = (int) result.getTotalElements();
        Pagination pagination = new Pagination(page, total, 10, limit, request);

        return new ListData<>(result.getContent(), pagination);
    }

    /**
     * 해시(기본키)로 단건 데이터를 조회
     * @param hash
     * @return
     */
    public CrawledData get(Integer hash) {
        return repository.findById(hash).orElseThrow(NotFoundException::new);
    }
}
