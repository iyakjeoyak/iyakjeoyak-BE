package com.example.demo.module.mail.dto.payload;

import lombok.Data;

@Data
public class EmailVerifyPayload {
    private String email;
    private String authCode;
}
