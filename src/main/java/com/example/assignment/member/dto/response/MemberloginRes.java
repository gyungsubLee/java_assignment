package com.example.assignment.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그인 응답 DTO")
public class MemberloginRes {

    @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public static MemberloginRes from(String token) {
        MemberloginRes response  = new MemberloginRes();
        response.token = token;
        return response;
    }
}
