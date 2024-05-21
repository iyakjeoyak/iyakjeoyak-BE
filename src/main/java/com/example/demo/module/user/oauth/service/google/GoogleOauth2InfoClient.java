package com.example.demo.module.user.oauth.service.google;

import com.example.demo.module.user.oauth.dto.google.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "google-oauth2-info-client", url = "https://www.googleapis.com")
public interface GoogleOauth2InfoClient {
    @GetMapping(value = "/oauth2/v2/userinfo")
    GoogleUserInfo getUserInfo(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token);
}
