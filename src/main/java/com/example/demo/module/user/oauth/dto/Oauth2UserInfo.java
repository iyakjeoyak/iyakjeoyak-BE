package com.example.demo.module.user.oauth.dto;

import com.example.demo.module.user.entity.Gender;
import com.example.demo.module.user.entity.SocialType;

public interface Oauth2UserInfo {
    String getId();
    String getEmail();
    String getNickname();
    String getImagePath();

    Gender getGender();
    SocialType getSocialType();
}
