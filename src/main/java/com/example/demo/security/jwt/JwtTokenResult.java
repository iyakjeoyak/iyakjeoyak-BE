package com.example.demo.security.jwt;

import lombok.Data;

@Data
public class JwtTokenResult {

    public JwtTokenResult(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // 엑세스 토큰
    private String accessToken;

    // 리프레쉬 토큰
    private String refreshToken;
}
