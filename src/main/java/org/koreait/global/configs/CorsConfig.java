package org.koreait.global.configs;

import org.springframework.context.annotation.Bean; // Bean 등록 애노테이션 임포트
import org.springframework.context.annotation.Configuration; // 설정 클래스 선언용 애노테이션 임포트
import org.springframework.web.cors.CorsConfiguration; // CORS 설정을 위한 CorsConfiguration 클래스 임포트
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // URL 패턴별 CORS 설정 소스 클래스 임포트
import org.springframework.web.filter.CorsFilter; // CORS 필터 구현체 임포트

@Configuration // 이 클래스가 스프링 설정 클래스임을 선언
public class CorsConfig {

    @Bean  // CorsFilter 빈으로 등록
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // URL 기반 CORS 설정 소스 생성
        CorsConfiguration config = new CorsConfiguration(); // CORS 정책 설정 객체 생성
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedOrigin("*"); // 모든 출처(origin) 허용

        source.registerCorsConfiguration("/api/v1/**", config); // /api/v1/** 경로에 CORS 설정 적용
        source.registerCorsConfiguration("/file/**", config);  // /file/**  경로에 CORS 설정 적용

        return new CorsFilter(source); // 설정된 소스를 사용하는 CorsFilter 반환
    }
}
