package com.example.demo.module.user.oauth.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.repository.ImageRepository;
import com.example.demo.module.user.entity.SocialUser;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.oauth.dto.OAuthTokenResponse;
import com.example.demo.module.user.oauth.dto.Oauth2UserInfo;
import com.example.demo.module.user.oauth.dto.google.GoogleUserInfo;
import com.example.demo.module.user.oauth.dto.kakao.KakaoUserInfo;
import com.example.demo.module.user.repository.SocialUserRepository;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.security.jwt.JwtTokenPayload;
import com.example.demo.security.jwt.JwtTokenResult;
import com.example.demo.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
//@Primary
@Transactional(readOnly = true)
public class Oauth2RestServiceImpl implements Oauth2Service{

    private final SocialUserRepository socialUserRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ImageRepository imageRepository;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URL;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Override
    @Transactional
    public JwtTokenResult loginByKakao(String code) {

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        String requestUrl = "https://kauth.kakao.com/oauth/token";

        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KAKAO_REDIRECT_URL);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(params ,headers);

        // postForEntity -> code ->
        ResponseEntity<OAuthTokenResponse> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST , request, OAuthTokenResponse.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
        }

        return createTokenByKakaoToken(responseEntity.getBody().getAccessToken());
    }

    @Override
    @Transactional
    public JwtTokenResult loginByGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        String requestUrl = "https://oauth2.googleapis.com/token";

        params.put("code", code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_REDIRECT_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<OAuthTokenResponse> responseEntity = restTemplate.postForEntity(requestUrl, params, OAuthTokenResponse.class);

        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
        }

        OAuthTokenResponse body = responseEntity.getBody();

        return createTokenByGoogleToken(body.getAccessToken());
    }

    @Transactional
    public JwtTokenResult createTokenByGoogleToken(String token) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + token);

        HttpEntity request = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<GoogleUserInfo> responseEntity = restTemplate.exchange("https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET, request, GoogleUserInfo.class);

        log.info("body = {}", responseEntity.getBody());

        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
        }

        return loginAndCreateToken(responseEntity.getBody());
    }

    @Transactional
    public JwtTokenResult createTokenByKakaoToken(String token) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<KakaoUserInfo> responseEntity = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, KakaoUserInfo.class);

        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
        }

        return loginAndCreateToken(responseEntity.getBody());
    }

    @Transactional
    public JwtTokenResult loginAndCreateToken(Oauth2UserInfo userInfo) {
        SocialUser socialUser = socialUserRepository.findSocialUserBySocialEmail(userInfo.getEmail()).orElse(null);
        if (ObjectUtils.isEmpty(socialUser)) {
            //빈객체 저장
            User user = User.builder()
                    .gender(userInfo.getGender())
                    .image(
                            imageRepository.save(
                                    Image.builder().fullPath(userInfo.getImagePath()).build())
                    )
                    .build();
            userRepository.save(user);

            // social user 저장
            socialUser = SocialUser.builder()
                    .user(user)
                    .imageUrl(userInfo.getImagePath())
                    .socialId(userInfo.getId())
                    .socialType(userInfo.getSocialType())
                    .socialEmail(userInfo.getEmail())
                    .build();
            socialUserRepository.save(socialUser);
        }

        JwtTokenPayload jwtTokenPayload = JwtTokenPayload.builder()
                .userId(socialUser.getUser().getUserId())
                .socialEmail(socialUser.getSocialEmail())
                .build();

        return jwtUtil.createAccessAndRefreshToken(jwtTokenPayload);
    }
}
