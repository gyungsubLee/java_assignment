package com.example.assignment.common.exception;

import com.example.assignment.common.exception.custom.InvalidAuthHeaderException;
import com.example.assignment.common.exception.custom.InvalidCredentialsException;
import com.example.assignment.common.exception.custom.InvalidTokenException;
import com.example.assignment.common.exception.custom.MemberAlreadyExistsException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 인증(Auth)
    INVALID_AUTH_HEADER(Category.AUTH, HttpStatus.UNAUTHORIZED, "A001", "Authorization 헤더가 Bearer 타입이 아닙니다.", InvalidAuthHeaderException.class),
    INVALID_TOKEN(Category.AUTH, HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 토큰입니다.", InvalidTokenException.class),
    EXPIRED_TOKEN(Category.AUTH, HttpStatus.UNAUTHORIZED, "A003", "토큰이 만료되었습니다.", ExpiredJwtException.class),
    MALFORMED_TOKEN(Category.AUTH, HttpStatus.UNAUTHORIZED, "A004", "손상된 토큰입니다.", MalformedJwtException.class),
    AUTHENTICATION_REQUIRED(Category.AUTH, HttpStatus.UNAUTHORIZED, "A008", "인증이 필요합니다.", AuthenticationCredentialsNotFoundException.class),

    // 회원 ( Member )
    MEMBER_ALREADY_EXISTS(Category.MEMBER, HttpStatus.CONFLICT, "M001", "이미 가입된 사용자 입니다.", MemberAlreadyExistsException.class),
    INVALID_CREDENTIALS(Category.MEMBER, HttpStatus.UNAUTHORIZED, "M002", "아이디 또는 비밀번호가 올바르지 않습니다.", InvalidCredentialsException.class),

    // 서버 오류
    INTERNAL_SERVER_ERROR(Category.SERVER, HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 오류가 발생했습니다.", null);


    private final Category category;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final Class<? extends Throwable> exceptionClass;

    public enum Category {
        AUTH, MEMBER, SERVER
    }
}
