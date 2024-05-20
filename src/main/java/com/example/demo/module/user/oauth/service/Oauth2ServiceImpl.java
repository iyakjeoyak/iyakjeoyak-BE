package com.example.demo.module.user.oauth.service;

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
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Oauth2ServiceImpl implements Oauth2Service {
    private final CustomKakaoOauth2Client kakaoOauth2Client;
    private final CustomGoogleOauth2Client googleOauth2Client;
    private final SocialUserRepository socialUserRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ImageRepository imageRepository;

    //이하 Oauth 설정
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
    private final String grantType = "authorization_code";

    @Override
    @Transactional
    public JwtTokenResult loginByKakao(String code) {
        OAuthTokenResponse token = kakaoOauth2Client.getToken(grantType, KAKAO_CLIENT_ID, KAKAO_REDIRECT_URL, code);
        return createTokenByKakaoToken("Bearer " + token.getAccessToken());
    }

    @Override
    @Transactional
    public JwtTokenResult loginByGoogle(String code) {
        OAuthTokenResponse token = googleOauth2Client.getToken(grantType, GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, GOOGLE_REDIRECT_URL, code);
        return createTokenByGoogleToken("Bearer " + token.getAccessToken());
    }

    @Transactional
    public JwtTokenResult createTokenByGoogleToken(String token) {
        GoogleUserInfo userInfo = googleOauth2Client.getUserInfo(token);
        return loginAndCreateToken(userInfo);
    }

    @Transactional
    public JwtTokenResult createTokenByKakaoToken(String token) {
        KakaoUserInfo userInfo = kakaoOauth2Client.getUserInfo(token);
        return loginAndCreateToken(userInfo);
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
