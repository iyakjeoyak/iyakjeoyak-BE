package com.example.demo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "리뷰 정보를 찾을 수 없습니다."),
    MEDICINE_NOT_FOUND(HttpStatus.BAD_REQUEST, "영양제 정보를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "이미지 정보를 찾을 수 없습니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "북마크 정보를 찾을 수 없습니다."),
    DECLARATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "신고 정보를 찾을 수 없습니다."),
    PHARMACY_NOT_FOUND(HttpStatus.BAD_REQUEST, "약국 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}