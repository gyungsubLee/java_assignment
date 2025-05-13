package com.example.assignment.member.dto.request;


import com.example.assignment.member.dto.EmailPasswordBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원 로그인 요청 DTO")
public class MemberloginReq extends EmailPasswordBase {
}

