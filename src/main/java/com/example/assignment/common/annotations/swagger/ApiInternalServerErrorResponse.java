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
@ApiResponse(responseCode = "500", description = "서버 내부 오류",
        content = @Content(mediaType = "application/json",
                schema = @Schema(
                        example = """
                                {
                                  \"error\": {
                                    \"code\": \"INTERNAL_SERVER_ERROR\",
                                    \"message\": \"서버 내부 오류가 발생했습니다.\"
                                  }
                                }
                                """
                )))
public @interface ApiInternalServerErrorResponse {
}
