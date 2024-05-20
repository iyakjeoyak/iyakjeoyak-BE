package com.example.demo.module.user.oauth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoAccount {
    @JsonProperty("profile")
    private Profile profile;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("email")
    private String email;
}
