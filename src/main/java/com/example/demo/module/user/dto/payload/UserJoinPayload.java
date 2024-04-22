package com.example.demo.module.user.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class UserJoinPayload {

    // TODO 아이디 벨리데이션
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    // 8~16자 대 소문자 숫자 특수문자 사용
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;
    @NotBlank(message = "성별 선택은 필수 입력 값입니다.")
    private String gender;

    private Integer age;

    //TODO tag
    private List<Long> tag;

}
