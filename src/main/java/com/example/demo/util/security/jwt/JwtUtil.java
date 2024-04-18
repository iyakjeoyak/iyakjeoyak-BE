package com.example.demo.util.security.jwt;

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

    public JwtUtil (
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    public String createAccessToken(JwtTokenPayload user) {
        return createToken(user, accessTokenExpTime);
    }

    private String createToken(JwtTokenPayload user, long expireTime) {
        // 사용자 유저 정보를 클레임에 삽입한다, key value 형식으로 이루어져 있음
        Claims claims = Jwts.claims();
        claims.put("userId" ,user.getUserId());
        claims.put("username" ,user.getUsername());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidty = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                // 생성 시기?
                .setIssuedAt(Date.from(now.toInstant()))
                // 소멸 시기?
                .setExpiration(Date.from(tokenValidty.toInstant()))
                //DS256이 왜 안 되는지
                .signWith(key, SignatureAlgorithm.HS256)
                // 토큰을 생성해 String으로 리턴
                .compact();
    }

    /*
     * token에서 userId 추출
     * */
    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    /*
     * JWT 유효성 검증
     * */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

        } catch (ExpiredJwtException e) {

        } catch (UnsupportedJwtException e) {

        } catch (IllegalArgumentException e) {

        }
        return false;
    }

    /*
     * JWT 클레임 추출
     * */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
