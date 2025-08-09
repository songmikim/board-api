package org.koreait.global.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 스프링 설정 클래스임을 명시
public class MvcConfig implements WebMvcConfigurer { // WebMvcConfigurer 구현

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스 요청 경로 /upload/**를 실제 파일 시스템 경로 file:upload/에 매핑
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:upload/");
    }
}
