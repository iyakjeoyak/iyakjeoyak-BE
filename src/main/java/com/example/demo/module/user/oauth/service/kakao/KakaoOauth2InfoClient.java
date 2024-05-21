package com.example.demo.module.user.oauth.service.kakao;

import com.example.demo.module.user.oauth.dto.kakao.KakaoUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-oauth2-client",url = "https://kapi.kakao.com")
public interface KakaoOauth2InfoClient {
    @GetMapping(value = "/v2/user/me", headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    KakaoUserInfo getUserInfo(@RequestHeader(value = "Authorization") String token);
}
