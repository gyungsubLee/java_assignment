package com.example.assignment.member.dto;

import com.example.assignment.member.domain.Member;
import com.example.assignment.member.domain.Role;
import lombok.Getter;

@Getter
public class MemberInfoRes {

    private Long id;
    private Role role;
    private String username;
    private String nickname;
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
