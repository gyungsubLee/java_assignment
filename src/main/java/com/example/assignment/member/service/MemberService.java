package com.example.assignment.member.service;

import com.example.assignment.common.auth.JwtTokenProvider;
import com.example.assignment.common.exception.custom.InvalidEmailAndPasswordException;
import com.example.assignment.common.exception.custom.MemberAlreadyExistsException;
import com.example.assignment.member.domain.Member;
import com.example.assignment.member.domain.Role;
import com.example.assignment.member.dto.*;
import com.example.assignment.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원 가입 처리
     *
     * <p>사용 가능 권한: MEMBER, ADMIN</p>
     *
     * @param request {@link MemberSignupReq} 회원 가입 요청 DTO <br>
     * @param role {@link Role} 사용자 권한 (USER 또는 ADMIN) <br>
     * @return {@link MemberSignupRes} 회원 가입 성공 응답 DTO <br>
     * @throws MemberAlreadyExistsException 이미 존재하는 이메일일 경우 예외 발생
     */
    public MemberSignupRes signup(MemberSignupReq request, Role role) {
        if (isValidEmail(request.getEmail())) {
            throw new MemberAlreadyExistsException();
        }

        Member member = Member.builder()
                .username(request.getUsername())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        memberRepository.save(member);
        return MemberSignupRes.from(member);
    }

    /**
     * 로그인 처리 (JWT 발급)
     *
     * <p>사용 가능 권한: MEMBER, ADMIN</p>
     *
     * @param request {@link MemberloginReq} 로그인 요청 DTO <br>
     * @return {@link MemberloginRes} JWT 응답 DTO <br>
     * @throws InvalidEmailAndPasswordException 이메일 또는 비밀번호 불일치 시 예외 발생
     */
    public MemberloginRes login(MemberloginReq request) {
        Member findMember = memberRepository.findByEmailOrThrow(request.getEmail());

        if (!isValidMember(request.getPassword(), findMember.getPassword())) {
            throw new InvalidEmailAndPasswordException();
        }

        String token = jwtTokenProvider.createToken(
                findMember.getEmail(),
                String.valueOf(findMember.getRole())
        );

        return MemberloginRes.from(token);
    }

    /**
     * 특정 이메일로 회원 정보 조회
     *
     * <p>사용 가능 권한: ADMIN</p>
     *
     * @param email 조회할 회원 이메일 <br>
     * @return {@link MemberInfoRes} 회원 정보 DTO
     */
    public MemberInfoRes findMemberByEmail(String email) {
        return MemberInfoRes.from(memberRepository.findByEmailOrThrow(email));
    }

    /**
     * 전체 회원 목록 조회
     *
     * <p>사용 가능 권한: ADMIN</p>
     *
     * @return {@link List} of {@link MemberInfoRes} 전체 회원 목록
     */
    public List<MemberInfoRes> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberInfoRes::from)
                .toList();
    }

    /**
     * 이메일로 회원 삭제
     *
     * <p>사용 가능 권한: MEMBER(본인), ADMIN(모든 회원)</p>
     *
     * @param email 삭제할 회원 이메일
     */
    public void deleteByEmail(String email) {
        memberRepository.findByEmailOrThrow(email);
        memberRepository.deleteByEmail(email);
    }

    /**
     * 회원 권한(ROLE) 변경
     *
     * <p>사용 가능 권한: ADMIN</p>
     *
     * @param email 대상 회원 이메일 <br>
     * @param newRole {@link Role} 새로 부여할 권한 (USER 또는 ADMIN)
     */
    public void updateMemberRole(String email, Role newRole) {
        memberRepository.findByEmailOrThrow(email);
        memberRepository.updateRole(email, newRole);
    }

    /**
     * 이메일 중복 여부 확인
     *
     * @param email 확인할 이메일 <br>
     * @return 중복 여부 (true: 이미 존재함)
     */
    private boolean isValidEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 비밀번호 일치 여부 확인
     *
     * @param rawPassword 입력한 평문 비밀번호 <br>
     * @param encodedPassword 저장된 암호화된 비밀번호 <br>
     * @return 비밀번호 일치 여부
     */
    private boolean isValidMember(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}