package com.example.demo.module.user.oauth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Profile {
    @JsonProperty("profile_image_url")
    private String imageUrl;
}
