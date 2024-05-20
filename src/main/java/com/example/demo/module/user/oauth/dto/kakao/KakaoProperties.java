package com.example.demo.module.user.oauth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoProperties {
    @JsonProperty("nickname")
    private String nickname;
}
