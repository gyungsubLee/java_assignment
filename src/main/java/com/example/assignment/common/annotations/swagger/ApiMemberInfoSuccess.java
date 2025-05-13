package com.example.assignment.common.annotations.swagger;

import com.example.assignment.member.dto.response.MemberInfoRes;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 요청 성공",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = MemberInfoRes.class)))
})
public @interface ApiMemberInfoSuccess {
}
