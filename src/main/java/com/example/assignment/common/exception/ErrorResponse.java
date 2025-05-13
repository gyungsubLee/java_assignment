package com.example.assignment.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Schema(hidden = true) // Swagger 문서에서는 숨김
public record ErrorResponse<T> (
        ErrorDetail<T> error
) implements DefaultErrorResponse {

    public static <T> ErrorResponse<T> from(ErrorCode errorCode, T message) {
        if (errorCode == null) {
            log.error("ErrorResponse: errorCode is null");
            throw new IllegalArgumentException("ErrorCode 필수 값입니다.");
        }
        return new ErrorResponse<>(new ErrorDetail<>(errorCode.name(), message));
    }

    public record ErrorDetail<T>(String code, T message) {}
}