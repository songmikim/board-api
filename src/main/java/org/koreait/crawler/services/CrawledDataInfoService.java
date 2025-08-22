package org.koreait.crawler.services;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawledData;
import org.koreait.crawler.entities.QCrawledData;
import org.koreait.crawler.repositories.CrawledDataRepository;
import org.koreait.global.exceptions.NotFoundException;
import org.koreait.global.search.CommonSearch;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CrawledDataInfoService {
    private final CrawledDataRepository repository;

    /**
     * 크롤링된 데이터 목록을 조회
     * @param search 공통 검색 정보
     * @return 검색 조건에 맞는 모든 데이터
     */
    public List<CrawledData> getList(CommonSearch search) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");

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

        Iterable<CrawledData> result = repository.findAll(andBuilder, sort);
        return StreamSupport.stream(result.spliterator(), false).toList();
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
