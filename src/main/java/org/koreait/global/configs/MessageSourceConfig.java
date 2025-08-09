package org.koreait.global.configs;

import org.springframework.context.MessageSource; // 메시지 소스 인터페이스 import
import org.springframework.context.annotation.Bean; // 빈 등록 어노테이션 import
import org.springframework.context.annotation.Configuration; // 설정 클래스 어노테이션 import
import org.springframework.context.support.ReloadableResourceBundleMessageSource; // 리소스를 동적으로 다시 로드 가능한 메시지 소스 클래스 import
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean; // 메시지 국제화를 위한 validator 설정용 클래스 import

@Configuration // 스프링 설정 클래스
public class MessageSourceConfig {

    @Bean // MessageSource 빈 등록
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/valid"); // 메시지 파일 경로 지정 (classpath 기준)
        messageSource.setDefaultEncoding("UTF-8"); // 파일 인코딩 설정
        return messageSource;
    }

    @Bean // Validator에 메시지소스를 주입하기 위한 빈
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource()); // 위에서 정의한 메시지 소스 사용
        return validator;
    }
}
