package com.example.assignment.member.dto;

import lombok.Getter;

@Getter
public class MemberloginRes {
    private String token;

    public static MemberloginRes from(String token) {
        MemberloginRes response  = new MemberloginRes();
        response.token = token;
        return response;
    }
}
