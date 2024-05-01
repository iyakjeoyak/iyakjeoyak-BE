package com.example.demo.module.user.dto.result;

import com.example.demo.module.image.dto.result.ImageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserSimpleResult {

    @Schema(description = "유저 PK")
    private Long userId;

    @Schema(description = "유저 닉네임")
    private String nickname;

    @Schema(description = "이미지 정보")
    private ImageResult image;
}
