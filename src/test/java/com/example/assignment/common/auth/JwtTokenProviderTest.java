package com.example.assignment.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import static org.assertj.core.api.Assertions.*;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private final  String fakeKeyBase64 = Base64.getEncoder().encodeToString(
                "fakeKeyThatIsVerySecureAndLongEnoughForHS512JustForTestingOnly1234567890!".repeat(2).getBytes());

    private final int expiration = 1800; // 30분

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(fakeKeyBase64, expiration);
    }

    @Test
    @DisplayName("JWT 토큰 생성 시 이메일과 역할(ROLE)이 정상적으로 포함되어야 한다")
    void createToken_ShouldContainEmailAndRole() {
        // given
        String email = "admin@example.com";
        String role = "ADMIN";

        // when
        String token = jwtTokenProvider.createToken(email, role);

        // then
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtTokenProvider.getSECRET_KEY())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo(email);
        assertThat(claims.get("role", String.class)).isEqualTo(role);
        assertThat(claims.getExpiration()).isAfter(new Date());
    }

    @Test
    @DisplayName("만료된 토큰은 파싱 시 ExpiredJwtException 예외를 던진다")
    void parseExpiredToken_ShouldThrowException() {
        // given
        String email = "expired@example.com";
        String role = "MEMBER";
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - 1000); // 이미 만료된 시점

        String expiredToken = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiredDate) // 과거 시간으로 설정
                .signWith(jwtTokenProvider.getSECRET_KEY())
                .compact();

        // when & then
        assertThatThrownBy(() ->
                Jwts.parserBuilder()
                        .setSigningKey(jwtTokenProvider.getSECRET_KEY())
                        .build()
                        .parseClaimsJws(expiredToken)
        ).isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("잘못된 키로 서명된 토큰을 검증하면 SignatureException 예외가 발생한다")
    void parseToken_WithWrongKey_ShouldFail() {
        // given
        String token = jwtTokenProvider.createToken("test@invalid.com", "ADMIN");

        String another_key = "fakeKeyThatIsVerySecureAndLongEnoufeafeafestingOnly1234567890!".repeat(2);

        // 잘못된 키로 시도
        Key wrongKey = new SecretKeySpec(another_key.getBytes(), "HmacSHA512");

        // when & then
        assertThatThrownBy(() ->
                Jwts.parserBuilder()
                        .setSigningKey(wrongKey)
                        .build()
                        .parseClaimsJws(token)
        ).isInstanceOf(SignatureException.class);
    }

}