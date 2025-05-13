package com.example.assignment.member.dto.response;

import com.example.assignment.member.domain.Member;
import com.example.assignment.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "회원 정보 응답 DTO") // 클래스 전체에 대한 설명
public class MemberInfoRes {

    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "회원 권한", example = "ADMIN") // Enum 타입의 권한 필드
    private Role role;

    @Schema(description = "회원 이름", example = "hong123") // 사용자명
    private String username;

    @Schema(description = "회원 닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "회원 이메일", example = "hong@example.com")
    private String email;

    public static MemberInfoRes from(Member member) {
        MemberInfoRes response = new MemberInfoRes();
        response.id = member.getId();
        response.role = member.getRole();
        response.username = member.getUsername();
        response.nickname = member.getNickname();
        response.email = member.getEmail();
        return response;
    }
}
