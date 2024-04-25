package com.example.demo.module.user.dto.payload;

import com.example.demo.module.hashtag.dto.result.HashtagResult;
import com.example.demo.module.user.entity.Gender;
import com.example.demo.module.user.entity.UserHashtag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserEditPayload {
    @Schema(description = "성별 Enum 타입 (FEMALE, MALE, SECRET)")
    private Gender gender;

    @Schema(description = "자기소개")
    private String introduce;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "나이")
    private Integer age;

    @Schema(description = "관심 태그 리스트")
    private List<HashtagResult> userHashtagList = new ArrayList<>();
}
