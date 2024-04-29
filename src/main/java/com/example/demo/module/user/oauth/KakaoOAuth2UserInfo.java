package com.example.demo.module.user.oauth;

import java.util.Map;

public class KakaoOAuth2UserInfo extends Oauth2UserInfo{
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickName() {

        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        if (account.isEmpty()) {
            throw new IllegalArgumentException("찾는 유저 정보가 없습니다.");
        }

        // 형변환 하는 이유는 뭘까?
        Map<String, Object> profile = (Map<String, Object>) account.get("profiles");

        if (profile.isEmpty()) {
            throw new IllegalArgumentException("찾는 유저 정보가 없습니다.");
        }

        return (String) profile.get("nickname");
    }

    @Override
    public String getImageUrl() {

        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        if (account.isEmpty()) {
            throw new IllegalArgumentException("찾는 유저 정보가 없습니다.");
        }

        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if(account.isEmpty()) {
            throw new IllegalArgumentException("찾는 유저 정보가 없습니다");
        }
        return (String) profile.get("thumbnail_image_url");
    }
}
