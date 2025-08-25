package org.koreait.crawler.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.koreait.crawler.controllers.RequestCrawling;
import org.koreait.crawler.entities.CrawledData;
import org.koreait.crawler.entities.CrawlerConfig;
import org.koreait.crawler.repositories.CrawledDataRepository;
import org.koreait.crawler.repositories.CrawlerConfigRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CrawlingService {
    private final CrawledDataRepository repository;
    private final RestTemplate restTemplate;
    private final ObjectMapper om;
    private final CrawlerConfigRepository configRepository;
    private final CrawlerSettingService settingService;

    @Value("${api.server.url}")
    private String apiUrl;

    /**
     * 크롤링 요청을 외부 API(Flask) 서버에 전달하고,
     * 결과 데이터를 CrawledData 엔티티로 변환 후 DB에 저장하는 메서드
     */
    public List<CrawledData> process(RequestCrawling form){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            String body = om.writeValueAsString(form);

            HttpEntity<String > request = new HttpEntity<>(body, headers);

            ResponseEntity<List> response = restTemplate.postForEntity(URI.create(apiUrl + "/crawler"), request, List.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return null;
            }

            List<CrawledData> items = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Object data : response.getBody()) {
                Map<String, Object> item = (Map<String, Object>) data;
                String link = normalizeLink((String) item.get("link"));
                int hash = Objects.hash(link);
                LocalDate date = item.get("date") == null ? null : LocalDate.parse((String) item.get("date"), formatter);
                String title = (String) item.get("title");
                String content = (String) item.get("content");
                boolean html = (boolean) item.get("is_html");

                Object imageObj = item.get("image");
                List<String> images = null;
                if (imageObj instanceof List<?> list) {
                    images = list.stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .toList();
                } else if (imageObj instanceof String str) {
                    images = List.of(str);
                }

                CrawledData _item = repository.findById(hash).orElseGet(CrawledData::new);
                _item.setHash(hash);
                _item.setLink(link);
                _item.setDate(date);
                _item.setTitle(title);
                _item.setContent(content);
                _item.setImage(images == null ? null : String.join(",", images));
                _item.setHtml(html);
                items.add(_item);

            }
            repository.saveAllAndFlush(items);
            return items;

        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * jsessionid가 포함된 경우 제거하여 링크를 정규화
     * @param link 원본 링크
     * @return 정규화된 링크
     */
    private String normalizeLink(String link) {
        if (link == null) {
            return null;
        }

        // 세미콜론으로 연결된 jsessionid 제거
        link = link.replaceAll("(?i);jsessionid=[^?]*", "");

        int qIndex = link.indexOf('?');
        if (qIndex != -1) {
            String base = link.substring(0, qIndex);
            String query = Arrays.stream(link.substring(qIndex + 1).split("&"))
                    .filter(p -> !p.toLowerCase().startsWith("jsessionid="))
                    .collect(Collectors.joining("&"));
            link = query.isEmpty() ? base : base + "?" + query;
        }

        return link;
    }

    /**
     * 24시간마다 실행되는 스케줄러
     * - 스케줄러가 ON 상태일 때만 동작
     * - DB에 저장된 CrawlerConfig 목록을 불러와 순차적으로 크롤링 실행
     */
    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRate = 6L)
    public void scheduledJob() {
        if (!settingService.isSchedulerEnabled()) {
            return;
        }

        List<CrawlerConfig> configs = configRepository.findAll();
        for (CrawlerConfig config : configs) {
            process(config.toRequest());
        }
    }
}
