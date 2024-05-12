package com.example.demo.module.user.oauth;

import com.example.demo.module.user.entity.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    // oauth2 로그인 시 키가 되는 필드
    private String nameAttributeKey;
    // 소셜 로그인 정보
    private Oauth2UserInfo oauth2UserInfo;

    @Builder // <- 이렇게도 사용이 가능한가? 일단 해보자
    private OAuthAttributes(String nameAttributeKey, Oauth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes){

        if (socialType == SocialType.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }
}
