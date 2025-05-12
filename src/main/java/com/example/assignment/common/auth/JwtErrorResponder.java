package com.example.assignment.common.auth;

import com.example.assignment.common.exception.ErrorCode;
import com.example.assignment.common.exception.ErrorResponse;
import com.example.assignment.common.exception.ExceptionResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtErrorResponder {

    private final Map<ErrorCode, String> errorResponseCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        try {
            ErrorCode errorCode = ExceptionResolver.resolveErrorCode(ex);
            response.setStatus(errorCode.getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            String json = getOrCreateCachedErrorResponse(request, errorCode);

            response.getWriter().write(json);
            response.getWriter().flush();
        } catch (IOException e) {
            log.error("JwtErrorResponder 응답 중 오류 발생", e);
        }
    }

    private String getOrCreateCachedErrorResponse(HttpServletRequest request, ErrorCode errorCode) {
        return errorResponseCache.computeIfAbsent(errorCode, code -> {
            try {
                String traceId = UUID.randomUUID().toString();
                String path = request.getRequestURI();
                String method = request.getMethod();

                ErrorResponse<String> errorResponse = ErrorResponse.from(code, code.getMessage());
                return objectMapper.writeValueAsString(errorResponse);
            }
            catch (Exception e) {
                log.error("ErrorResponse 변환 실패: {}", code, e);
                return "{\"code\":\"" + code.name() + "\",\"message\":\"" + code.getMessage() + "\"}";
            }
        });
    }
}