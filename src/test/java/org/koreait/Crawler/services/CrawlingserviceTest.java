package org.koreait.Crawler.services;

import org.junit.jupiter.api.Test;
import org.koreait.crawler.controllers.RequestCrawling;
import org.koreait.crawler.entities.CrawledData;
import org.koreait.crawler.services.CrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles({"default","test"})
public class CrawlingserviceTest {
    @Autowired
    private CrawlingService service;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void crawlingTest() {
        Map<String, Object> item = Map.of(
                "link", "https://example.com/1",
                "date", "2024-01-01",
                "title", "Title1",
                "content", "Content1",
                "is_html", false
        );
        ResponseEntity<List> response = ResponseEntity.ok(List.of(item));
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), eq(List.class)))
                .thenReturn(response);

        RequestCrawling form = new RequestCrawling();
        form.setUrl("https://example.com");

        List<CrawledData> results = service.process(form);
        assertEquals(1, results.size());
        assertEquals("https://example.com/1", results.get(0).getLink());
    }
}
