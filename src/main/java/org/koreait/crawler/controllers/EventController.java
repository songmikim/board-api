package org.koreait.crawler.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.koreait.crawler.entities.CrawledData;
import org.koreait.crawler.services.CrawledDataInfoService;
import org.koreait.global.search.CommonSearch;
import org.koreait.global.search.ListData;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@Tag(name="환경 행사 API", description = "크롤링된 환경 행사 정보를 조회하는 기능을 제공")
public class EventController {

    private final CrawledDataInfoService infoService;

    @Operation(summary = "환경 행사 목록 조회", description = "저장된 모든 환경 행사 정보를 페이징 없이 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "환경 행사 목록"),
            @ApiResponse(responseCode = "204", description = "조회된 데이터 없음")
    })
    @GetMapping
    public ListData<CrawledData> list(@ModelAttribute CommonSearch search) {
        System.out.println( "///////////////==//"+ infoService.getList(search));
        return infoService.getList(search);
    }

    @Operation(summary = "환경 행사 상세 조회", description = "해시값으로 단일 환경 행사 정보를 조회")
    @Parameter(name = "hash", required = true, in = ParameterIn.PATH, description = "환경 행사 데이터를 식별하는 해시값")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "환경 행사 상세 정보"),
            @ApiResponse(responseCode = "404", description = "환경 행사 정보를 찾을 수 없음")
    })
    @GetMapping("/{hash}")
    public CrawledData info(@PathVariable("hash") Integer hash) {
        System.out.println(hash + "================111====" + infoService.get(hash));
        return infoService.get(hash);
    }
}
