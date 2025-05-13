package com.example.assignment.member.controller;

import com.example.assignment.common.annotations.swagger.*;
import com.example.assignment.member.domain.Role;
import com.example.assignment.member.dto.request.MemberSignupReq;
import com.example.assignment.member.dto.response.MemberInfoRes;
import com.example.assignment.member.dto.response.MemberSignupRes;
import com.example.assignment.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자 API", description = "관리자 권한을 위한 회원 관리 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "관리자 계정 생성", description = "관리자 권한으로 새로운 관리자를 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "관리자 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "username": "admin123",
                              "nickname": "홍길동",
                              "roles": ["ADMIN"]
                            }
                            """)
            )
    )
    @ApiAcoountAlreadyExistErrorResponse
    @ApiInternalServerErrorResponse
    public ResponseEntity<MemberSignupRes> signup(@RequestBody MemberSignupReq request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.signup(request, Role.ADMIN));
    }

    @GetMapping("/members")
    @Operation(summary = "전체 회원 조회", description = "관리자가 전체 회원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MemberInfoRes.class))
                    ))
    })
    @ApiErrorResponses
    public ResponseEntity<List<MemberInfoRes>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @GetMapping("/members/{email}")
    @Operation(summary = "회원 단건 조회", description = "관리자가 특정 회원의 정보를 조회합니다.")
    @ApiMemberInfoSuccess
    @ApiErrorResponses
    public ResponseEntity<MemberInfoRes> getMemberByEmail(
            @Parameter(description = "조회할 회원 이메일") @PathVariable String email) {
        return ResponseEntity.ok(memberService.findMemberByEmail(email));
    }

    @PatchMapping("/members/{email}/role")
    @Operation(summary = "회원 권한 변경", description = "관리자가 특정 회원의 권한(ROLE)을 변경합니다.")
    @ApiMemberInfoSuccess
    @ApiErrorResponses
    public ResponseEntity<MemberInfoRes> updateMemberRole(
            @Parameter(description = "회원 이메일") @PathVariable String email,
            @Parameter(description = "변경할 권한") @RequestParam Role newRole) {
        return ResponseEntity.ok().body(memberService.updateMemberRole(email, newRole));
    }

    @DeleteMapping("/members/{email}")
    @Operation(summary = "회원 삭제", description = "관리자가 회원을 강제로 삭제합니다.")
    @ApiErrorResponses
    public ResponseEntity<Void> deleteMember(
            @Parameter(description = "삭제할 회원 이메일") @PathVariable String email) {
        memberService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }
}
