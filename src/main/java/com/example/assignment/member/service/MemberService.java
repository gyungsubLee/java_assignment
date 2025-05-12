package com.example.assignment.member.service;

import com.example.assignment.common.exception.custom.MemberAlreadyExistsException;
import com.example.assignment.member.domain.Member;
import com.example.assignment.member.dto.MemberSignupReq;
import com.example.assignment.member.dto.MemberSignupRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public MemberSignupRes signup(MemberSignupReq request) {

        if (isValidEmail(request.email())) {
            throw new MemberAlreadyExistsException();
        }

        Member member = Member.builder()
                .username(request.username())
                .nickname(request.nickname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        memberRepository.save(member);

        return MemberSignupRes.from(member);
    }

    private boolean isValidEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}