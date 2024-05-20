package com.example.demo.module.user.oauth.service;

import com.example.demo.security.jwt.JwtTokenResult;

public interface Oauth2Service {
    JwtTokenResult loginByKakao(String code);

    JwtTokenResult loginByGoogle(String code);

}
