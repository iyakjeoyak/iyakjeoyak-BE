package com.example.demo.module.user.oauth.dto.google;

import com.example.demo.module.user.entity.Gender;
import com.example.demo.module.user.entity.SocialType;
import com.example.demo.module.user.oauth.dto.Oauth2UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleUserInfo implements Oauth2UserInfo {

    @JsonProperty("id")
    private String id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("picture")
    private String imgUrl;
    @JsonProperty("gender")
    private String gender;

    @Override
    public String getImagePath() {
        return imgUrl;
    }
    @Override
    public SocialType getSocialType() {
        return SocialType.GOOGLE;
    }
    @Override
    public Gender getGender() {
        return gender == null ? Gender.SECRET : Gender.valueOf(gender.toUpperCase());
    }
}
