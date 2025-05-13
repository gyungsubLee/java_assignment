package com.example.assignment.member.service;

import com.example.assignment.common.auth.JwtTokenProvider;
import com.example.assignment.common.exception.custom.InvalidCredentialsException;
import com.example.assignment.common.exception.custom.MemberAlreadyExistsException;
import com.example.assignment.member.domain.Member;
import com.example.assignment.member.domain.Role;
import com.example.assignment.member.dto.request.MemberSignupReq;
import com.example.assignment.member.dto.request.MemberloginReq;
import com.example.assignment.member.dto.response.MemberInfoRes;
import com.example.assignment.member.dto.response.MemberSignupRes;
import com.example.assignment.member.dto.response.MemberloginRes;
import com.example.assignment.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private MemberSignupReq memberSignupReq;
    private MemberSignupReq adminSignupReq;
    private Member member;
    private Member admin;

    @BeforeEach
    void setUp() {
        memberSignupReq = new MemberSignupReq();
        memberSignupReq.setEmail("test@email.com");
        memberSignupReq.setPassword("test123");
        memberSignupReq.setUsername("member");
        memberSignupReq.setNickname("member_test");

        member = Member.builder()
                .email("test@email.com")
                .password("encoded-password")
                .username("member")
                .nickname("member_test")
                .role(Role.MEMBER)
                .build();
        member.setId(1L);


        adminSignupReq = new MemberSignupReq();
        adminSignupReq.setEmail("admin@email.com");
        adminSignupReq.setPassword("encoded-password");
        adminSignupReq.setUsername("admin");
        adminSignupReq.setNickname("admin_test");

        admin = Member.builder()
                .email("admin@email.com")
                .password("encoded-password")
                .username("admin")
                .nickname("admin_test")
                .role(Role.ADMIN)
                .build();
        member.setId(2L);
    }

    @Test
    @DisplayName("일반 유저(MEMBER) 회원가입 성공")
    void member_Signup_Success() {
        // given
        when(memberRepository.existsByEmail(memberSignupReq.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(memberSignupReq.getPassword())).thenReturn("encoded-password");

        // void 메서드에 대한 mocking
        doAnswer(invocation -> {
            Member arg = invocation.getArgument(0);
            arg.setId(1L);
            return null;
        }).when(memberRepository).save(any(Member.class));

        // when
        MemberSignupRes res = memberService.signup(memberSignupReq, Role.MEMBER);

        // then
        assertThat(res.getUsername()).isEqualTo("member");
        assertThat(res.getNickname()).isEqualTo("member_test");
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("관리자(ADMIN) 회원가입 성공")
    void admin_Signup_Success() {
        // given
        when(memberRepository.existsByEmail(adminSignupReq.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(adminSignupReq.getPassword())).thenReturn("encoded-password");

        // void 메서드에 대한 mocking
        doAnswer(invocation -> {
            Member arg = invocation.getArgument(0);
            arg.setId(2L);
            return null;
        }).when(memberRepository).save(any(Member.class));

        // when
        MemberSignupRes res = memberService.signup(adminSignupReq, Role.ADMIN);

        // then
        assertThat(admin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(res.getUsername()).isEqualTo(admin.getUsername());
        assertThat(res.getNickname()).isEqualTo(admin.getNickname());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void signup_DuplicateEmail_Fail() {
        when(memberRepository.existsByEmail(memberSignupReq.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> memberService.signup(memberSignupReq, Role.MEMBER))
                .isInstanceOf(MemberAlreadyExistsException.class);
    }



    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        MemberloginReq loginReq = new MemberloginReq();
        loginReq.setEmail("test@email.com");
        loginReq.setPassword("test123");

        when(memberRepository.findByEmailOrThrow(loginReq.getEmail())).thenReturn(member);
        when(passwordEncoder.matches(loginReq.getPassword(), member.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createToken(member.getEmail(), String.valueOf(member.getRole())))
                .thenReturn("jwt-token");

        MemberloginRes res = memberService.login(loginReq);

        assertThat(res.getToken()).isEqualTo("jwt-token");
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void login_InvalidPassword_Fail() {
        MemberloginReq loginReq = new MemberloginReq();
        loginReq.setEmail("test@email.com");
        loginReq.setPassword("wrong123");

        when(memberRepository.findByEmailOrThrow(loginReq.getEmail())).thenReturn(member);
        when(passwordEncoder.matches(loginReq.getPassword(), member.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> memberService.login(loginReq))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("회원 이메일로 조회 성공")
    void findMemberByEmail_Success() {
        when(memberRepository.findByEmailOrThrow(member.getEmail())).thenReturn(member);

        MemberInfoRes res = memberService.findMemberByEmail(member.getEmail());

        assertThat(res.getEmail()).isEqualTo(member.getEmail());
        assertThat(res.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    @DisplayName("회원 전체 조회")
    void findAll_Success() {
        when(memberRepository.findAll()).thenReturn(List.of(member));

        List<MemberInfoRes> resList = memberService.findAll();

        assertThat(resList).hasSize(1);
        assertThat(resList.get(0).getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void deleteByEmail_Success() {
        when(memberRepository.findByEmailOrThrow(member.getEmail())).thenReturn(member);
        doNothing().when(memberRepository).deleteByEmail(member.getEmail());

        memberService.deleteByEmail(member.getEmail());

        verify(memberRepository).deleteByEmail(member.getEmail());
    }

    @Test
    @DisplayName("회원 역할 변경 성공")
    void updateMemberRole_Success() {
        when(memberRepository.updateRole(member.getEmail(), Role.ADMIN)).thenReturn(
                Member.builder()
                        .email(member.getEmail())
                        .role(Role.ADMIN)
                        .build()
        );

        MemberInfoRes res = memberService.updateMemberRole(member.getEmail(), Role.ADMIN);

        assertThat(res.getRole()).isEqualTo(Role.ADMIN);
    }
}