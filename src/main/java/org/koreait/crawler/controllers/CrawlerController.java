package org.koreait.crawler.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawledData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crawler")
@Tag(name="크롤러 API", description = "환경 행사 데이터를 크롤링하여 등록 및 수정하는 기능을 제공")
public class CrawlerController {

    private final HttpServletRequest request;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "환경 행사 데이터 등록 및 수정", description = "POST 요청시 새로운 환경 행사 데이터를 등록하고 PATCH 요청시 기존 데이터를 수정")
    @ApiResponse(responseCode = "200", description = "등록 또는 수정된 환경 행사 데이터")
    @RequestMapping(method = {RequestMethod.PATCH, RequestMethod.POST})
    public CrawledData update() {
        String mode = request.getMethod().equalsIgnoreCase("PATCH") ? "update" : "register";

        return null;
    }

}
