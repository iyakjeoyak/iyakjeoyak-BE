package com.example.demo.module.user.dto.payload;

import com.example.demo.module.user.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserJoinPayload {

    // TODO 아이디 벨리데이션
    @Schema(description = "로그인 ID")
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,12}$", message = "아이디는 4~12자의 영문 또는 숫자만 허용됩니다.")
    private String username;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 영문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
    private String password;

    @Schema(description = "닉네임")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Schema(description = "성별 Enum 타입 (FEMALE, MALE, SECRET)")
    @NotNull(message = "성별 선택은 필수 입력 값입니다.")
    private Gender gender;

    @Schema(description = "나이")
    // notEmpty는 스트링만
    @NotNull(message = "나이는 필수 입력 값입니다.")
    @Min(value = 1, message = "나이는 1세 이상이어야 합니다.")
    @Max(value = 100, message = "나이는 100세 이하여야 합니다.")
    private Integer age;

    // 중간 테이블(user + role)
    @Schema(description = "유저 권한 리스트")
    @NotEmpty(message = "유저의 권한이 들어 가지 않았습니다.")
    private List<Long> userRoleList = new ArrayList<>();


    //중간 테이블(user + hashtag)
    // no value present는 어떻게 해결할 것인가 ..?
    @Schema(description = "관심 태그 리스트")
    @NotEmpty(message = "관심 태그가 들어 가지 않았습니다.")
    private List<Long> userHashtagList = new ArrayList<>();

}
