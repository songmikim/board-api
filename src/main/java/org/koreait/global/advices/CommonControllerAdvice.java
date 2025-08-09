package org.koreait.global.advices;

import lombok.RequiredArgsConstructor;
import org.koreait.global.exceptions.CommonException;
import org.koreait.global.libs.Utils;
import org.koreait.global.rests.JSONError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor    //final 필드를 초기화하는 생성자를 자동 생성한다.
@RestControllerAdvice("org.koreait")    // org.koreait패키지 내의 모든 컨트롤러에 적용될 공통예외 처리 클래스임을 선언한다.
public class CommonControllerAdvice {
    private final Utils utils;  // 에러 메시지 조회 등에 사용할 유틸리티 객체를 주입받는다.

    @ExceptionHandler(Exception.class) // 모든 예외를 처리하는 핸들러 메서드임을 지정한다.
    public ResponseEntity<JSONError> errorHandler(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 기본 에러 코드는 500
        Object message = e.getMessage(); //예외의 메시지를 기본 에러 메시지로 가져온다.

        if (e instanceof CommonException commonException) {  // 사용자가 정의한 CommonException인 경우
            status = commonException.getStatus(); // 예외 객체에 설정된 HTTP 상태 코드를 가져온다.
            Map<String, List<String>> errorMessages = commonException.getErrorMessages(); // 커맨드 객체 검증 실패 메세지
            if (errorMessages != null) {
                message = errorMessages; // 검증 에러 메시지가 존재하면 메시지로 대체한다.
            } else {
                // 에러 코드로 관리되는 문구인 경우
                if (commonException.isErrorCode()) {  // 메시지가 에러 코드일때
                    message = utils.getMessage((String)message); // 유틸리티를 통해 다국어 메시지를 조회한다.
                }
            }
        } else if (e instanceof AuthorizationDeniedException) {  // Spring Security권한 거부 예외인 경우
            status = HttpStatus.UNAUTHORIZED; // 상태 코드를 401(인증 실패)로 설정한다.
            message = utils.getMessage("UnAuthorized"); // 유틸리티에서 권한 오류 메시지를 가져온다.
        }

        e.printStackTrace(); // 예외 스택 트레이스를 콘솔에 출력하여 디버깅에 도움을 준다.

        // 상태 코드와 메시지를 담은 JSONError 객체를 응답 본문으로 반환한다.
        return ResponseEntity.status(status).body(new JSONError(status, message));
    }
}
