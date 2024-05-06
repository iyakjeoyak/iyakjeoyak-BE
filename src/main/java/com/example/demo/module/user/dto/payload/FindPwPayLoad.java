package com.example.demo.module.user.dto.payload;

import lombok.Data;

@Data
public class FindPwPayLoad {
    private String email;
    private String authCode;
    private String newPassword;
}
