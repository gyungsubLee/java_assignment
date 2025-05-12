package com.example.assignment.member.controller;

import com.example.assignment.member.domain.Role;
import com.example.assignment.member.dto.MemberInfoRes;
import com.example.assignment.member.dto.MemberSignupReq;
import com.example.assignment.member.dto.MemberSignupRes;
import com.example.assignment.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 전용 회원 관리 API 컨트롤러
 * 사용 가능 권한: ADMIN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MemberService memberService;

    /**
     * 관리자 계정 생성
     *
     * @param request {@link MemberSignupReq} 관리자 회원가입 요청 DTO
     * @return {@link MemberSignupRes} 생성된 관리자 정보 DTO
     */
    @PostMapping("/signup")
    public ResponseEntity<MemberSignupRes> signup(@RequestBody MemberSignupReq request) {
        return ResponseEntity.ok().body(memberService.signup(request, Role.ADMIN));
    }

    /**
     * 전체 회원 목록 조회
     *
     * @return 회원 리스트 {@link List} of {@link MemberInfoRes}
     */
    @GetMapping("/members")
    public ResponseEntity<List<MemberInfoRes>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAll());
    }

    /**
     * 특정 회원 정보 조회
     *
     * @param email 회원 이메일
     * @return {@link MemberInfoRes} 회원 정보
     */
    @GetMapping("/members/{email}")
    public ResponseEntity<MemberInfoRes> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.findMemberByEmail(email));
    }

    /**
     * 회원 권한(Role) 변경
     *
     * @param email 대상 회원 이메일
     * @param newRole 새로운 권한 {@link Role}
     */
    @PatchMapping("/members/{email}/role")
    public ResponseEntity<Void> updateMemberRole(
            @PathVariable String email,
            @RequestParam Role newRole
    ) {
        memberService.updateMemberRole(email, newRole);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 삭제 (강제 탈퇴)
     *
     * @param email 삭제할 회원 이메일
     */
    @DeleteMapping("/members/{email}")
    public ResponseEntity<Void> deleteMember(@PathVariable String email) {
        memberService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }
}