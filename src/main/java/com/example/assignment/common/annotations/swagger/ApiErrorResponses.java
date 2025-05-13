package com.example.assignment.common.annotations.swagger;

import com.example.assignment.common.exception.ErrorResponseString;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 401 + 500 응답을 함께 포함하는 공통 오류 응답 어노테이션
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiUnauthorizedResponse
@ApiInternalServerErrorResponse
public @interface ApiErrorResponses {
}
