package com.example.assignment.common.auth;

import com.example.assignment.common.exception.custom.InvalidAuthHeaderException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtErrorResponder errorResponder;

    @Value("${jwt.secretKey}")
    private String secretKey;

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String CLAIM_ROLE = "role";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HEADER_AUTHORIZATION);

        try {
            if (token != null && token.startsWith(TOKEN_PREFIX)) {
                String jwtToken = token.substring(7);
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String role = String.valueOf(claims.get(CLAIM_ROLE));
                if (!role.startsWith(ROLE_PREFIX)) role = ROLE_PREFIX + role;

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            if (e instanceof InvalidAuthHeaderException) {
                log.warn("Authorization 헤더 형식 오류", e);
            } else if (e instanceof io.jsonwebtoken.JwtException) {
                log.warn("JWT 토큰 파싱 오류", e);
            } else {
                log.error("Unhandled Exception 발생", e);
            }
            errorResponder.sendErrorResponse(request, response, e);
        }
    }

    private Key getSigningKey() {
        return new SecretKeySpec(
                Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS512.getJcaName()
        );
    }
}