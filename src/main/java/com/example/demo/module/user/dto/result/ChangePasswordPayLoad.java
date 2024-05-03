package com.example.demo.module.user.dto.result;

import lombok.Data;

@Data
public class ChangePasswordPayLoad {
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
}
