package com.example.demo.module.user.dto.payload;

import com.example.demo.module.image.entity.Image;
import com.example.demo.module.user.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserJoinPayload {

    // TODO 아이디 벨리데이션
    // TODO email (naver, kakao, google, nate << social 가입을 안 하고 자체 서비스로 가입을 했다)
    // naver.com // social -> 자동으로 회원가입이 되게 만들거잖아요? -> 이미 있는 아이디라고 뜨겠죠 ?
    // 이미 아이디 있으니깐 -> 아이디 보여주면서 너 이걸로 로그인할래?
    // login,
    @Schema(description = "로그인 ID")
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
//    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]).[a-zA-Z]{2,3}$", message = "이메일 형식에 맞지 않습니다.")
    @Email(message = "이메일 형식만 가능합니다.")
    private String username;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 영문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
    private String password;

    private String confirmPassword;

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
