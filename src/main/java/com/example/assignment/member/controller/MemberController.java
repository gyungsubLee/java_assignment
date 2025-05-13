package com.example.assignment.member.controller;

import com.example.assignment.common.annotations.swagger.ApiInternalServerErrorResponse;
import com.example.assignment.common.annotations.swagger.ApiUnauthorizedResponse;
import com.example.assignment.member.domain.Role;
import com.example.assignment.member.dto.request.MemberSignupReq;
import com.example.assignment.member.dto.response.MemberSignupRes;
import com.example.assignment.member.dto.request.MemberloginReq;
import com.example.assignment.member.dto.response.MemberloginRes;
import com.example.assignment.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 일반 사용자 회원 관련 API
 *
 * 사용 가능 권한: GUEST (비로그인)
 */
@Tag(name = "회원 API", description = "일반 사용자 회원가입 및 로그인")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     *
     * <p>사용 가능 권한: 누구나</p>
     *
     * @param request {@link MemberSignupReq} 회원가입 요청 DTO <br>
     * @return {@link MemberSignupRes} 가입된 회원 정보 DTO
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "일반 사용자가 회원가입합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "유저 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberSignupRes.class)
            )
    )
    @ApiInternalServerErrorResponse
    public ResponseEntity<MemberSignupRes> signup(@RequestBody MemberSignupReq request) {
        return ResponseEntity.ok().body(memberService.signup(request, Role.MEMBER));
    }

    /**
     * 로그인
     *
     * <p>사용 가능 권한: 누구나</p>
     *
     * @param request {@link MemberloginReq} 로그인 요청 DTO <br>
     * @return {@link MemberloginRes} JWT 포함 응답 DTO
     */
    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "JWT 토큰을 발급받기 위한 로그인"
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberloginRes.class)
            )
    )
    @ApiUnauthorizedResponse
    public ResponseEntity<MemberloginRes> login(@RequestBody MemberloginReq request) {
        return ResponseEntity.ok().body(memberService.login(request));
    }
}