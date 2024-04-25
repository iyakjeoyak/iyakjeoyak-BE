package com.example.demo.module.user.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginPayload {

    @Schema(description = "로그인 ID")
    private String username;

    @Schema(description = "유저 비밀번호")
    private String password;

}
