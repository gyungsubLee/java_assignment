package com.example.assignment.member.dto;

import com.example.assignment.member.domain.Member;
import com.example.assignment.member.domain.Role;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemberSignupRes {
    private String username;
    private String nickname;
    private final List<Role> roles = new ArrayList<>();

    public static MemberSignupRes from(Member member) {
        MemberSignupRes response = new MemberSignupRes();
        response.username = member.getUsername();
        response.nickname = member.getNickname();
        response.roles.add(member.getRole());
        return response;
    }
}