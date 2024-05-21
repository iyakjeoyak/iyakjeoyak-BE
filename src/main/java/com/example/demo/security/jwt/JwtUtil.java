package com.example.demo.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.ZonedDateTime;

@Slf4j
@Component
public class JwtUtil {
    // lombok @Value 쓰지 말고 spring꺼 쓰기
    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;
    public final String[] allowedUrls = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/",
            "/error",
            "/users/signup",
            "/users/login",
            "/users/access-token",
            "/users/password",
            "/users/kakao-authcode",
            "/users/google-authcode",
            "/users/check/username/**",
            "/users/check/nickname/**",
            "/maps/**",
            "/maps**",
            "/categories",
            "/hashtags",
            "/bookmarks/medicine/**",
            "/top-users",
            "/auto-completes",
            "/medicines",
            "/medicines/**",
            "/mails/verify",
            "/mails/send/verify",
            "/images/**",
            "/actuator/**"
    };
    public final String[] onlyGetNotFilter = {
            "/medicine-hearts/",
            "/review-hearts/",
            "/hashtags",
            "/categories",
            "/bookmarks/medicine/",
            "/medicines/query",
            "/mails",
            "/images",
            "/users/check/username",
            "/users/check/nickname",
    };
    public final String[] onlyGetAllowUrl = {
            "/medicine-hearts/**",
            "/review-hearts/**",
            "/reviews/**",
            "/reviews",
    };

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time.access}") long accessTokenExpTime,
            @Value("${jwt.expiration_time.refresh}") long refreshTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    // access 토큰 단독
    public String createAccessToken(JwtTokenPayload user) {
        return createToken(user, "access", accessTokenExpTime);
    }


    // token 생성시 둘 다 만든다
    public JwtTokenResult createAccessAndRefreshToken(JwtTokenPayload tokenPayload) {
        String access = createToken(tokenPayload, "access", accessTokenExpTime);
        String refresh = createToken(tokenPayload, "refresh", refreshTokenExpTime);

        return new JwtTokenResult(access, refresh);
    }


    private String createToken(JwtTokenPayload user, String type, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("nickname", user.getNickname());
        claims.put("socialEmail", user.getSocialEmail());
        claims.put("tokenType", type);
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidty = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidty.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtTokenPayload getJwtTokenPayload(String token) {
        String username = parseClaims(token).get("username", String.class);
        String nickname = parseClaims(token).get("nickname", String.class);
        Long userId = parseClaims(token).get("userId", Long.class);

        return JwtTokenPayload.builder().username(username).nickname(nickname).userId(userId).build();
    }

    public boolean validateToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return claimsJws != null && claimsJws.getBody().get("tokenType").toString().equals("access");
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
