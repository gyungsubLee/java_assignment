package com.example.assignment.member.dto.response;

import com.example.assignment.member.domain.Member;
import com.example.assignment.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "회원가입 응답 DTO")
public class MemberSignupRes {

    @Schema(description = "사용자 이름", example = "user123")
    private String username;

    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "역할 목록", example = "[\"MEMBER or ADMIN\"]")
    private final List<Role> roles = new ArrayList<>();

    public static MemberSignupRes from(Member member) {
        MemberSignupRes response = new MemberSignupRes();
        response.username = member.getUsername();
        response.nickname = member.getNickname();
        response.roles.add(member.getRole());
        return response;
    }
}
