package com.example.assignment.member.dto.request;

import com.example.assignment.member.dto.EmailPasswordBase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원 생성 요청 DTO")
public class MemberSignupReq extends EmailPasswordBase {

        @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
        @Schema(description = "사용자 이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
        private String username;

        @NotBlank(message = "닉네임은 필수입니다.")
        @Schema(description = "사용자 닉네임", example = "닉네임1", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
        private String nickname;
}