package com.example.demo.module.user.oauth.service;

import com.example.demo.module.user.oauth.dto.OAuthTokenResponse;
import com.example.demo.module.user.oauth.dto.kakao.KakaoUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customKakaoOauth2Client",url = "https://")
public interface CustomKakaoOauth2Client {
    @PostMapping("kauth.kakao.com/oauth/token")
    OAuthTokenResponse getToken(@RequestParam(name = "grant_type") String grantType,
                                      @RequestParam(name = "client_id") String clientId,
                                      @RequestParam(name = "redirect_uri") String redirectUri,
                                      @RequestParam(name = "code") String code);

    @PostMapping(value = "kapi.kakao.com/v2/user/me", headers = "Content-type: application/json; charset=utf-8")
    KakaoUserInfo getUserInfo(@RequestHeader(name = "Authorization") String token);
}
