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
@ApiResponse(
        responseCode = "409",
        description = "이미 생성된 정보",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        example = """
                {
                  "error": {
                    "code": "MEMBER_ALREADY_EXISTS",
                    "message": "이미 가입된 정보 입니다."
                  }
                }
                """
                )
        )
)
public @interface ApiAcoountAlreadyExistErrorResponse {
}