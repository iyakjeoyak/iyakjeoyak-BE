package com.example.demo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    ErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}