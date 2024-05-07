package com.example.demo.module.user.dto.payload;

import com.example.demo.module.hashtag.dto.result.HashtagResult;
import com.example.demo.module.user.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserEditPayload {
    @Schema(description = "성별 Enum 타입 (FEMALE, MALE, SECRET)")
    @NotNull(message = "성별 선택은 필수 입력 값입니다.")
    private Gender gender;

    @Schema(description = "자기소개")
    private String introduce;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "나이")
    @NotNull(message = "나이는 필수 입력 값입니다.")
    @Min(value = 1, message = "나이는 1세 이상이어야 합니다.")
    @Max(value = 100, message = "나이는 100세 이하여야 합니다.")
    private Integer age;

    // Integer 숫자만?
    @Schema(description = "관심 태그 리스트")
    @NotEmpty
    private List<Integer> hashtagResultList = new ArrayList<>();

}
