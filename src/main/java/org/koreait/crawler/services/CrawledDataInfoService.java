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

    public List<CrawledData> getList() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    public CrawledData get(Integer hash) {
        return repository.findById(hash).orElseThrow(NotFoundException::new);
    }
}
