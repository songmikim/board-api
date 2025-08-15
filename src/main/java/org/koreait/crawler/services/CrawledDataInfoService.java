package org.koreait.crawler.services;

import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawledData;
import org.koreait.crawler.repositories.CrawledDataRepository;
import org.koreait.global.exceptions.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawledDataInfoService {
    private final CrawledDataRepository repository;

    /**
     * 크롤링된 데이터 전체 목록을 날짜 기준 내림차순으로 반환
     * @return
     */
    public List<CrawledData> getList() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "date"));
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
