package com.example.demo.module.user.oauth;


import java.util.Map;

public abstract class Oauth2UserInfo {

    protected Map<String, Object> attributes;

    public Oauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    // 소실 로그인에서 정보를 식별 -> 구글 = sub, 카카오 = id
    // 소셜에서 사용하고 싶은 정보를 get 메서드를 생성한다
    public abstract String getId();

    public abstract String getNickName();

    public abstract String getImageUrl();

}
