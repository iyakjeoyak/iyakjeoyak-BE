package com.example.demo.module.user.oauth.service.kakao;

import com.example.demo.module.user.oauth.dto.OAuthTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao-oauth2-token",url = "https://kauth.kakao.com")
public interface KakaoOauth2TokenClient {
    @PostMapping(value = "/oauth/token", headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    OAuthTokenResponse getToken(@RequestParam(value = "grant_type") String grantType,
                                      @RequestParam(value = "client_id") String clientId,
                                      @RequestParam(value = "redirect_uri") String redirectUri,
                                      @RequestParam(value = "code") String code);

}
