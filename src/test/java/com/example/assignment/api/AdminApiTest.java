package com.example.assignment.api;

import com.example.assignment.member.domain.Role;
import com.example.assignment.member.dto.response.MemberSignupRes;
import com.example.assignment.member.dto.response.MemberloginRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "JWT_SECRET_KEY=7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk7J6E7Iuc7YWM7Iqk7Yq47Jqp7YKk",
        "JWT_EXPIRATION=3600000" // 필요한 다른 키도 함께
})
public class AdminApiTest {

    @LocalServerPort
    int port;

    RestClient restClient;

    String memberToken;
    String adminToken;

    String memberEmail;
    String adminEmail;

    @BeforeEach
    void setUp() {
        this.restClient = RestClient.create("http://localhost:" + port);

        memberEmail = generateUniqueEmail("member");
        adminEmail = generateUniqueEmail("admin");

        // 회원가입
        signup(new MemberSignupReq("일반유저", "memebr123", memberEmail, "member123!"), "/api/v1/members/signup");
        signup(new MemberSignupReq("관리자", "admin123", adminEmail, "admin123!"), "/test/create-admin");

        // 로그인
        memberToken = login(new MemberLoginReq(memberEmail, "member123!")).getToken();
        adminToken = login(new MemberLoginReq(adminEmail, "admin123!")).getToken();
    }

    @Test
    @DisplayName("관리자 권한으로는 새로운 관리자를 생성할 수 있다")
    void shouldCreateAdmin_WhenRequestedByAdmin() {
        // given
        String newAdminEmail = generateUniqueEmail("newadmin");
        MemberSignupReq newAdminReq = new MemberSignupReq("홍길동1", "gildong123", newAdminEmail, "securePass123!");

        // when
        ResponseEntity<MemberSignupRes> response = postWithToken(
                "/api/v1/admin/signup", adminToken, newAdminReq, MemberSignupRes.class
        );

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getRoles()).contains(Role.ADMIN);
    }

    @Test
    @DisplayName("유저 권한으로는 새로운 관리자를 생성할 수 없다 (403 Forbidden)")
    void shouldFailToCreateAdmin_WhenRequestedByUser() {
        // given
        String unauthorizedEmail = generateUniqueEmail("unauth");
        MemberSignupReq newAdminReq = new MemberSignupReq("홍길동2", "gildong234", unauthorizedEmail, "securePass123!");

        // when
        Throwable thrown = catchThrowable(() ->
                postWithToken("/api/v1/admin/signup", memberToken, newAdminReq, MemberSignupRes.class)
        );

        // then
        assertThat(thrown).isInstanceOf(HttpClientErrorException.Forbidden.class);
        assertThat(thrown).hasMessageContaining("403");
    }



    private <T> ResponseEntity<T> postWithToken(String uri, String token, Object body, Class<T> responseType) {
        return restClient.post()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .retrieve()
                .toEntity(responseType);
    }

    private MemberSignupRes signup(MemberSignupReq request, String url) {
        return restClient.post()
                .uri(url)
                .body(request)
                .retrieve()
                .body(MemberSignupRes.class);
    }

    private MemberloginRes login(MemberLoginReq request) {
        return restClient.post()
                .uri("/api/v1/members/login")
                .body(request)
                .retrieve()
                .body(MemberloginRes.class);
    }

    private String generateUniqueEmail(String prefix) {
        return prefix + System.currentTimeMillis() + "@xample.com";
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class MemberSignupReq {
        private String username;
        private String nickname;
        private String email;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class MemberLoginReq {
        private String email;
        private String password;
    }
}