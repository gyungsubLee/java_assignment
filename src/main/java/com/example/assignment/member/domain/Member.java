package com.example.assignment.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private Long id;

    private final String username;

    private final String nickname;

    private final String email;

    private final String password;

    private final Role role;

    public void setId(Long id) {
        this.id = id;
    }

    @Builder
    public Member(String username, String nickname, String email, String password, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role != null ? role : Role.USER;
    }
}