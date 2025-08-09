package org.koreait.global.configs;

import com.fasterxml.jackson.databind.ObjectMapper; // JSON 직렬화/역직렬화를 위한 ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Java 8 날짜/ 시간 타입 모듈 등록
import com.querydsl.jpa.impl.JPAQueryFactory; // QueryDSL을 사용한 JPA 쿼리 팩토리
import jakarta.persistence.EntityManager; // JPA 엔티티 메니저
import jakarta.persistence.PersistenceContext; // PersistenceContext 애노테이션
import org.modelmapper.ModelMapper; // 객체 매핑을 위한 ModelMapper
import org.modelmapper.convention.MatchingStrategies; // 엄격 매칭 전략 설정
import org.springframework.context.annotation.Bean;  // Bean 등록 애노테이션
import org.springframework.context.annotation.Configuration; // 설정 클래스임을 선언
import org.springframework.context.annotation.Lazy; // 지연 초기화 설정
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory; // RestTemplate 커넥션 팩토리
import org.springframework.web.client.RestTemplate; // HTTP 통신을 위한 RestTemplate

@Configuration  // 스프링 설정 클래스 등록
public class BeanConfig {

    @PersistenceContext // 엔티티 매니저를 스프링이 주입하도록 설정
    private EntityManager em;

    @Lazy // 필요 시점에 빈을 초기화하도록 지연 설정
    @Bean // ModelMapper를 빈으로 등록
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();  // ModelMapper 객체 생성
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // 엄격한 매핑 전략 적용

        return mapper; // 설정된 매퍼 반환
    }

    @Lazy // 지연초기화 설정
    @Bean // ObjectMapper를 빈으로 등록
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper(); // ObjectMapper 객체 생성
        om.registerModule(new JavaTimeModule());  // JavaTimeModule 등록으로 LocalDateTime 등 지원
        return om; // 설정된 ObjectMapper 반환
    }

    @Lazy
    @Bean
    public RestTemplate restTemplate() {
        // HttpComponentsClientHttpRequestFactory 사용하여 HTTP 요청/응답 처리
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    @Lazy
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        //  생성자에 주입된 EntityManager를 사용하여 쿼리 팩토리 생성
        return new JPAQueryFactory(em);
    }

}
