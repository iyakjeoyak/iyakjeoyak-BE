package com.example.demo.module.user.oauth.dto.kakao;

import com.example.demo.module.user.entity.Gender;
import com.example.demo.module.user.entity.SocialType;
import com.example.demo.module.user.oauth.dto.Oauth2UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoUserInfo implements Oauth2UserInfo {
    //선택
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    //필수
    @JsonProperty("properties")
    private KakaoProperties properties;

    @Override
    public String getId() {
        return kakaoAccount.getId();
    }
    @Override
    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    @Override
    public String getNickname() {
        return properties.getNickname();
    }

    @Override
    public String getImagePath() {
        return kakaoAccount.getImageUrl();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
    @Override
    public Gender getGender() {
        return kakaoAccount.getGender() == null ? Gender.SECRET : Gender.valueOf(kakaoAccount.getGender().toUpperCase());
    }
}
