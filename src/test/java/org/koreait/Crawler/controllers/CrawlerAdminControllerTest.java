package org.koreait.Crawler.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.koreait.crawler.controllers.RequestCrawling;
import org.koreait.crawler.entities.CrawledData;
import org.koreait.crawler.services.CrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"default","test"})
public class CrawlerAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private CrawlingService crawlingService;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testCrawlOnce() throws Exception {
        CrawledData data = new CrawledData();
        data.setHash(1);
        data.setLink("https://example.com");
        when(crawlingService.process(any(RequestCrawling.class))).thenReturn(List.of(data));

        RequestCrawling form = new RequestCrawling();
        form.setUrl("https://example.com");
        String body = om.writeValueAsString(form);

        String response = mockMvc.perform(post("/api/v1/crawler/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<CrawledData> items = om.readValue(response, new TypeReference<List<CrawledData>>(){});
        assertEquals(1, items.size());
        assertEquals("https://example.com", items.get(0).getLink());
    }
}
