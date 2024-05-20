package com.example.demo.module.user.oauth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoAccount {
    @JsonProperty("id")
    private String id;
    @JsonProperty("profile_image")
    private String imageUrl;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("email")
    private String email;
}
