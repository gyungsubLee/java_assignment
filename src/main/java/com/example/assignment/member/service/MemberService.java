package com.example.assignment.member.service;

import com.example.assignment.common.auth.JwtTokenProvider;
import com.example.assignment.common.exception.EmailNotFoundException;
import com.example.assignment.common.exception.custom.InvalidEmailAndPasswordException;
import com.example.assignment.common.exception.custom.MemberAlreadyExistsException;
import com.example.assignment.member.domain.Member;
import com.example.assignment.member.dto.MemberSignupReq;
import com.example.assignment.member.dto.MemberSignupRes;
import com.example.assignment.member.dto.MemberloginReq;
import com.example.assignment.member.dto.MemberloginRes;
import com.example.assignment.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public MemberSignupRes signup(MemberSignupReq request) {

        if (isValidEmail(request.getEmail())) {
            throw new MemberAlreadyExistsException();
        }

        Member member = Member.builder()
                .username(request.getUsername())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        memberRepository.save(member);

        return MemberSignupRes.from(member);
    }


    public MemberloginRes login(MemberloginReq request) {

        if (isEmailNotFound(request.getEmail())) {
            throw new InvalidEmailAndPasswordException();
        }

        Member findMember = memberRepository.findByEmail(request.getEmail());

        if(!isValidMember(request.getPassword(), findMember.getPassword())) {
            throw new InvalidEmailAndPasswordException();
        }

        String token = jwtTokenProvider.createToken(findMember.getEmail(), String.valueOf(findMember.getRole()));

        return MemberloginRes.from(token);
    }

    private boolean isValidEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private boolean isEmailNotFound(String email) {
        return !memberRepository.existsByEmail(email);
    }

    private boolean isValidMember(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}