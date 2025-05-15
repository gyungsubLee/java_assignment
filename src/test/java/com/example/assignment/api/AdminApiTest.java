package com.example.assignment.api;

import com.example.assignment.member.domain.Role;
import com.example.assignment.member.dto.response.MemberSignupRes;
import com.example.assignment.member.dto.response.MemberloginRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.*;

public class AdminApiTest {

    RestClient restClient = RestClient.create("http://0.0.0.0:8080");

    String memberToken;
    String adminToken;

    String memberEmail;
    String adminEmail;

    @DisplayName("일반 유저, 관리자 토큰 생성")
    @BeforeEach
    void setUp() {
        memberEmail = generateUniqueEmail("member");
        adminEmail = generateUniqueEmail("admin");

        MemberSignupReq memberSignupReq = new MemberSignupReq("일반유저", "memebr123", memberEmail, "member123!");
        MemberSignupReq adminSignupReq = new MemberSignupReq("관리자", "admin123", adminEmail, "admin123!");

        // 회원가입
        signup(memberSignupReq, "/api/v1/members/signup");
        signup(adminSignupReq, "/test/create-admin");

        // 로그인
        MemberloginRes memberRes = login(new MemberLoginReq(memberEmail, "member123!"));
        memberToken = memberRes.getToken();

        MemberloginRes adminRes = login(new MemberLoginReq(adminEmail, "admin123!"));
        adminToken = adminRes.getToken();
    }

    @DisplayName("관리자 권한으로는 새로운 관리자를 생성할 수 있다")
    @Test
    void shouldCreateNewAdmin_WhenAdminRequests() {
        // given
        String newAdminEmail = generateUniqueEmail("newadmin");
        MemberSignupReq newAdminReq = new MemberSignupReq(
                "홍길동1",
                "gildong123",
                newAdminEmail,
                "securePass123!"
        );

        // when
        ResponseEntity<MemberSignupRes> response = restClient.post()
                .uri("/api/v1/admin/signup")
                .header("Authorization", "Bearer " + adminToken)
                .body(newAdminReq)
                .retrieve()
                .toEntity(MemberSignupRes.class);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getRoles().get(0)).isEqualTo(Role.ADMIN);
    }

    @DisplayName("유저 권한으로는 새로운 관리자를 생성할 수 없다 (AccessDeniedException 발생 확인)")
    @Test
    void shouldThrowAccessDeniedException_WhenMemberTriesToCreateAdmin() {
        // given
        String unauthorizedEmail = generateUniqueEmail("unauthorized");
        MemberSignupReq newAdminReq = new MemberSignupReq(
                "홍길동2",
                "gildong1234",
                unauthorizedEmail,
                "securePass123!"
        );

        // when
        Throwable thrown = catchThrowable(() -> restClient.post()
                .uri("/api/v1/admin/signup")
                .header("Authorization", "Bearer " + memberToken)
                .body(newAdminReq)
                .retrieve()
                .body(MemberSignupRes.class)
        );

        // then
        assertThat(thrown).isNotNull();
        assertThat(thrown).isInstanceOfAny(HttpClientErrorException.Forbidden.class);
    }

    private String generateUniqueEmail(String prefix) {
        return prefix + System.currentTimeMillis() + "@xample.com";
    }

    MemberSignupRes signup(MemberSignupReq request, String url) {
        return restClient.post()
                .uri(url)
                .body(request)
                .retrieve()
                .body(MemberSignupRes.class);
    }

    MemberloginRes login(MemberLoginReq request) {
        return restClient.post()
                .uri("/api/v1/members/login")
                .body(request)
                .retrieve()
                .body(MemberloginRes.class);
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class MemberSignupReq {
    private String username;
    private String nickname;
    private String email;
    private String password;
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class MemberLoginReq {
    private String email;
    private String password;
}