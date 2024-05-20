package com.example.demo.module.user.oauth.service;

import com.example.demo.module.user.oauth.dto.OAuthTokenResponse;
import com.example.demo.module.user.oauth.dto.google.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customGoogleOauth2Client",url = "https://")
public interface CustomGoogleOauth2Client {
    @PostMapping("oauth2.googleapis.com/token")
    OAuthTokenResponse getToken(@RequestParam(name = "grant_type") String grantType,
                                      @RequestParam(name = "client_id") String clientId,
                                      @RequestParam(name = "redirect_uri") String redirectUri,
                                      @RequestParam(name = "code") String code);

    @PostMapping(value = "www.googleapis.com/oauth2/v2/userinfo", headers = "Content-type: application/json; charset=utf-8")
    GoogleUserInfo getUserInfo(@RequestHeader(name = "Authorization") String token);
}
