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
            "/user/signup",
            "/user/login",
            "/user/createAccessByRefresh",
            "/user/findPassword",
            "/user/getKakaoAuthCode",
            "/user/getGoogleAuthCode",
            "/user/getGoogleAuthCode",
            "/user/check/username/**",
            "/user/check/nickname/**",
            "/map/**",
            "/map",
            "/category",
            "/hashtag",
            "/bookmark/medicine/**",
            "/topUser",
            "/auto-complete",
            "/medicine",
            "/medicine/**",
            "/mail/verify",
            "/mail/send/verify",
            "/image/**",
            "/actuator/**"
    };
    public final String[] onlyGetAllow = {
            "/review",
            "/review/**",
            "/heart",
            "/heart/**",
            "/hashtag",
            "/category",
            "/map",
            "/bookmark/medicine/",
            "/medicine",
            "/mail",
            "/image",
            "/map/",
            "/user/check/username",
            "/user/check/nickname"
//            "/review/top",
//            "/review/**"
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

        JwtTokenResult jwtTokenResult = new JwtTokenResult(access, refresh);

        return jwtTokenResult;
    }


    private String createToken(JwtTokenPayload user, String type, long expireTime) {
        // 사용자 유저 정보를 클레임에 삽입한다, key value 형식으로 이루어져 있음
        /*
         *  JwtTokenPayload : userId, username, nickname
         *
         * */
        Claims claims = Jwts.claims();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("nickname", user.getNickname());
        claims.put("tokenType", type);
//        claims.put("", user.)
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
    //TODO 굳이 유저 아이디만 줄 필요가 없다.
    public JwtTokenPayload getJwtTokenPayload(String token) {
//        return parseClaims(token).get("userId", Long.class);
        String username = parseClaims(token).get("username", String.class);
        String nickname = parseClaims(token).get("nickname", String.class);
        Long userId = parseClaims(token).get("userId", Long.class);

        return JwtTokenPayload.builder().username(username).nickname(nickname).userId(userId).build();
    }


    /*
     * JWT 유효성 검증
     * */
    public boolean validateToken(String token) {
        Jws<Claims> claimsJws = null;
        try {
            claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

        } catch (Exception e) {
            return false;
        }
        return claimsJws != null && claimsJws.getBody().get("tokenType").toString().equals("access");
    }

    /*
     * JWT 클레임 추출
     * */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            //TODO 에러 메세지
            return e.getClaims();
        }
    }
}
