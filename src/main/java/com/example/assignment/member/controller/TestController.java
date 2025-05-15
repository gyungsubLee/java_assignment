package com.example.assignment.member.controller;

import com.example.assignment.common.annotations.swagger.ApiAcoountAlreadyExistErrorResponse;
import com.example.assignment.common.annotations.swagger.ApiInternalServerErrorResponse;
import com.example.assignment.member.domain.Role;
import com.example.assignment.member.dto.request.MemberSignupReq;
import com.example.assignment.member.dto.response.MemberSignupRes;
import com.example.assignment.member.service.MemberService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "테스용 관리자 생성 API", description = "관리자 권한 API 테스트를 위한 임시 API")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final MemberService memberService;

    // 테스트용 임시 관리자 생성
    @PostMapping("/create-admin")
    @ApiResponse(
            responseCode = "201",
            description = "관리자 계정 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "username": "홍길동",
                              "nickname": "admin-test",
                              "roles": ["AMDIN"]
                            }
                            """)
            )
    )

    @ApiAcoountAlreadyExistErrorResponse
    @ApiInternalServerErrorResponse
    public ResponseEntity<MemberSignupRes> createAdminAccountTest(@RequestBody MemberSignupReq request) {
        return ResponseEntity.ok().body(memberService.signup(request, Role.ADMIN));
    }
}
