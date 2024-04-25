package com.example.demo.module.user.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginPayload {

    private String username;
    private String password;

}
