package com.example.demo.module.user.oauth;

import java.util.Map;

public class GoogleOAuth2UserInfo extends Oauth2UserInfo{

    //requireargsconstructor을 사용했을 때 안 된 이유는 무엇일까?
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getNickName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public String getAge() {
        return null;
    }
}
