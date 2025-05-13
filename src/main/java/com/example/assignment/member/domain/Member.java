package com.example.assignment.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Member {
    private Long id;

    private  Role role;

    private final String username;

    private final String nickname;

    private final String email;

    private final String password;


    @Builder
    public Member(Role role, String username, String nickname, String email, String password) {
        this.role = role != null ? role : Role.MEMBER;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(Role newRole) {
        this.role = newRole;
    }
}