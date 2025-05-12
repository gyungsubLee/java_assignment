package com.example.assignment.member.controller;

import com.example.assignment.member.dto.MemberSignupReq;
import com.example.assignment.member.dto.MemberSignupRes;
import com.example.assignment.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberSignupRes> signup(@RequestBody MemberSignupReq request) {
        return ResponseEntity.ok().body(memberService.signup(request));
    }
}