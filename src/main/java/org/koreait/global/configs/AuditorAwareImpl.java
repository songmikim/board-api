package org.koreait.global.configs;

import lombok.RequiredArgsConstructor; // final 필드를 초기화 하는 생성자 자동 생성
import org.koreait.member.libs.MemberUtil; // 로그인 사용자 정보 조회 유틸
import org.springframework.data.domain.AuditorAware; // JPA 감사(Auditing)를 위한 인터페이스
import org.springframework.stereotype.Component; // Spring Bean 으로 등록하기 위한 애노테이션

import java.util.Optional; // null-safe 반환을 위한 Optional 클래스

@Component // 스프링 컨테이너에 Bean으로 등록하여 자동 주입 대상이 되도록 생성
@RequiredArgsConstructor // final 필드를 인자로 갖는 생성자를 롬복이 자동 생성
public class AuditorAwareImpl implements AuditorAware<String> { // AuditorAware 인터페이스 구현: 생성자/수정자 정보 제공

    private final MemberUtil memberUtil; // 로그인 여부 및 회원 정보 조회에 사용할 유틸 객체 주입

    @Override
    public Optional<String> getCurrentAuditor() { //  현재 감사 주체(작성자)로 사용할 사용자 식별자 반환
        // 로그인 상태이면 회원 이메일 반환, 아니면 null -> Optional.empty()처리
        return Optional.ofNullable(memberUtil.isLogin() ? memberUtil.getMember().getEmail() : null);
    }
}
