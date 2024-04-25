package com.example.demo.module.user.dto.payload;

import com.example.demo.module.user.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserJoinPayload {

    // TODO 아이디 벨리데이션
    @Schema(description = "로그인 ID")
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    // 8~16자 대 소문자 숫자 특수문자 사용
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Schema(description = "닉네임")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Schema(description = "성별 Enum 타입 (FEMALE, MALE, SECRET)")
    @NotBlank(message = "성별 선택은 필수 입력 값입니다.")
    private Gender gender;

    @Schema(description = "나이")
    private Integer age;

    // 중간 테이블(user + role)
    @Schema(description = "유저 권한 리스트")
    @NotEmpty
    private List<Long> userRoleList = new ArrayList<>();


    //중간 테이블(user + hashtag)
    @Schema(description = "관심 태그 리스트")
    @NotEmpty
    private List<Long> userHashtagList = new ArrayList<>();

}
