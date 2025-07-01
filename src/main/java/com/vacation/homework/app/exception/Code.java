package com.vacation.homework.app.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {
    // 기본 예외
    OK(0, HttpStatus.OK, "Ok"),
    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(10001, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(10002, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    UNAUTHORIZED(40000, HttpStatus.UNAUTHORIZED, "User unauthorized"),
    METHOD_NOT_ALLOWED(60000, HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed"),

    // API 관련 예외
    API_REQUEST_FAILED(14000, HttpStatus.SERVICE_UNAVAILABLE, "Failed to fetch data from external API"),
    API_RESPONSE_PARSE_ERROR(14001, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to parse API response"),
    API_RATE_LIMIT_EXCEEDED(14002, HttpStatus.TOO_MANY_REQUESTS, "API rate limit exceeded"),

    // JWT 관련 예외
    JWT_INVALID_SIGNATURE(40001, HttpStatus.UNAUTHORIZED, "Invalid JWT signature"),
    JWT_INVALID_TOKEN(40002, HttpStatus.UNAUTHORIZED, "Invalid JWT token"),
    JWT_EXPIRED_TOKEN(40003, HttpStatus.UNAUTHORIZED, "Expired JWT token"),
    JWT_UNSUPPORTED_TOKEN(40004, HttpStatus.UNAUTHORIZED, "Unsupported JWT token"),
    JWT_INVALID_CLAIMS(40005, HttpStatus.UNAUTHORIZED, "JWT claims string is empty"),
    INVALID_CREDENTIALS(40006, HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(40007, HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_MISMATCH(40008, HttpStatus.UNAUTHORIZED, "리프레시 토큰이 일치하지 않습니다."),


    //추가예외
    NOT_FOUND_USER(30000, HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    NOT_FOUND_HOMEWORK(30001, HttpStatus.NOT_FOUND, "해당 일기를 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(30002, HttpStatus.NOT_FOUND, "해당 코멘트를 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS(30003, HttpStatus.FORBIDDEN, "해당 리소스에 접근할 권한이 없습니다."),
    INVALID_ROLE(30004, HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."),
    DUPLICATE_AGREE(30005, HttpStatus.BAD_REQUEST, "이미 동의한 약관입니다."),
    DATABASE_ERROR(30006, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    HOMEWORK_ALREADY_EXISTS(30007, HttpStatus.BAD_REQUEST, "이미 작성한 일자입니다."),
    NOT_FOUND_HOMEWORK_OR_UNAUTHORIZED(30008, HttpStatus.NOT_FOUND, "존재하지 않거나 권한이 없습니다."),
    DUPLICATE_USER_ID(30009, HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),


    //토큰 관련 예외
    REFRESH_COOKIE_NOT_FOUND(50011, HttpStatus.UNAUTHORIZED ,"refresh token not found in cookie" ),
    INVALID_ACCESS_TOKEN(50005,HttpStatus.UNAUTHORIZED ,  "Invalid access token"),
    EMPTY_COOKIE(50009,HttpStatus.UNAUTHORIZED, "Empty COOKIE"),
    EMPTY_ACCESS_TOKEN(50010,HttpStatus.UNAUTHORIZED, "Access token Empty");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public static Code valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return Code.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return Code.INTERNAL_ERROR;
                    } else {
                        return Code.OK;
                    }
                });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}