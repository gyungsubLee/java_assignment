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
        responseCode = "403",
        description = "관리자 권한 필요",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        example = """
                {
                  "error": {
                     "code": "ACCESS_DENIED",
                     "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
                  }
                }
                """
                )
        )
)
public @interface ApiAccessDeniedException {
}





