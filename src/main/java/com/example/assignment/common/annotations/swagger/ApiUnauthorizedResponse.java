package com.example.assignment.common.annotations.swagger;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "401", description = "인증이 필요합니다",
        content = @Content(mediaType = "application/json",
                schema = @Schema(
                        example = """
                                {
                                  \"error\": {
                                    \"code\": \"AUTHENTICATION_REQUIRED\",
                                    \"message\": \"유효하지 않은 회원 정보입니다.\"
                                  }
                                }
                                """
                )))
public @interface ApiUnauthorizedResponse {
}
