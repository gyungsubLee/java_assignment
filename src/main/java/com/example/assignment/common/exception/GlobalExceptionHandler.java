package com.example.assignment.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<DefaultErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ExceptionResolver.resolveErrorCode(ex);
        return buildErrorResponse(errorCode, errorCode.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ExceptionResolver.resolveErrorCode(ex);
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));
        return buildErrorResponse(errorCode, fieldErrors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<DefaultErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorCode errorCode = ExceptionResolver.resolveErrorCode(ex);
        return buildErrorResponse(errorCode, errorCode.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<DefaultErrorResponse> handleUnknownException(Throwable ex, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return buildErrorResponse(errorCode, errorCode.getMessage());
    }

    @Schema(hidden = true)
    private <T> ResponseEntity<ErrorResponse<T>> buildErrorResponse(ErrorCode errorCode, T message) {
        log.error("error: {} - {}", errorCode.name(), errorCode.getMessage());

        ErrorResponse<T> errorResponse = ErrorResponse.from(errorCode, message);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }


    private <T> ResponseEntity<DefaultErrorResponse> buildErrorResponse(ErrorCode errorCode, String message) {
        log.error("error: {} - {}", errorCode.name(), errorCode.getMessage());

        DefaultErrorResponse errorResponse = ErrorResponseString.from(errorCode, message);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }
}