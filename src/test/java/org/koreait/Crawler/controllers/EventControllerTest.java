package org.koreait.Crawler.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.crawler.entities.CrawledData;
import org.koreait.crawler.repositories.CrawledDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"default","test"})
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CrawledDataRepository repository;

    @BeforeEach
    void init() {
        repository.deleteAll();

        LocalDate today = LocalDate.now();

        CrawledData data1 = new CrawledData();
        data1.setHash(1);
        data1.setLink("https://example.com/1");
        data1.setTitle("Event1");
        data1.setContent("Content1");
        data1.setDate(today.minusDays(1));
        repository.save(data1);

        CrawledData data2 = new CrawledData();
        data2.setHash(2);
        data2.setLink("https://example.com/2");
        data2.setTitle("Event2");
        data2.setContent("Content2");
        data2.setDate(today);
        repository.save(data2);
    }

    @Test
    @DisplayName("List crawled events")
    void listTest() throws Exception {
        String body = mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<CrawledData> items = om.readValue(body, new TypeReference<>() {});
        assertEquals(2, items.size());
        assertEquals(2, items.get(0).getHash());
    }

    @Test
    @DisplayName("Get event details")
    void infoTest() throws Exception {
        String body = mockMvc.perform(get("/api/v1/events/{hash}", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CrawledData item = om.readValue(body, CrawledData.class);
        assertEquals("Event1", item.getTitle());
    }

    @Test
    @DisplayName("Event not found returns 404")
    void notFoundTest() throws Exception {
        mockMvc.perform(get("/api/v1/events/{hash}", 999))
                .andExpect(status().isNotFound());
    }
}
