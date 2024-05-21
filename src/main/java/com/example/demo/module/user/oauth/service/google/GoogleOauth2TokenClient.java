package com.example.demo.module.user.oauth.service.google;

import com.example.demo.module.user.oauth.dto.OAuthTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-oauth2-token-client", url = "https://oauth2.googleapis.com")
public interface GoogleOauth2TokenClient {
    @PostMapping("/token")
    OAuthTokenResponse getToken(@RequestParam(name = "grant_type") String grantType,
                                @RequestParam(value = "client_id") String clientId,
                                @RequestParam(value = "client_secret") String clientSecret,
                                @RequestParam(value = "redirect_uri") String redirectUri,
                                @RequestParam(value = "code") String code);

}
