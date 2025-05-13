package com.example.assignment.common.exception;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "에러 응답 포맷")
@Getter
public class ErrorResponseString implements DefaultErrorResponse {

    @Schema(description = "에러 상세 정보")
    private ErrorDetail error;

    public ErrorResponseString(String code, String message) {
        this.error = new ErrorDetail(code, message);
    }

    public static ErrorResponseString from(ErrorCode errorCode, String message) {
        if (errorCode == null) {
            throw new IllegalArgumentException("ErrorCode 필수 값입니다.");
        }
        return new ErrorResponseString(errorCode.name(), message);
    }

    @Getter
    public static class ErrorDetail {

        private final String code;

        private final String message;

        public ErrorDetail(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}