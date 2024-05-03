package com.example.demo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.example.demo.global.exception.ErrorDefinition.ILLEGAL_ARGUMENT;
import static com.example.demo.global.exception.ErrorDefinition.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // DB 조회 실패 에러 코드
    USER_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "리뷰 정보를 찾을 수 없습니다."),
    MEDICINE_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "영양제 정보를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "이미지 정보를 찾을 수 없습니다."),
    HASHTAG_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "해쉬태그 정보를 찾을 수 없습니다."),
    BOOKMARK_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "북마크 정보를 찾을 수 없습니다."),
    DECLARATION_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "신고 정보를 찾을 수 없습니다."),
    PHARMACY_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "약국 정보를 찾을 수 없습니다."),


    // 로직 실패 에러코드
    REVIEW_DUPLICATION(BAD_REQUEST, ILLEGAL_ARGUMENT,"이미 후기를 작성한 영양제 입니다."),
    DECLARATION_DUPLICATION(BAD_REQUEST, ILLEGAL_ARGUMENT, "이미 신고한 리뷰입니다."),
    ACCESS_BLOCKED(FORBIDDEN, ILLEGAL_ARGUMENT,"작성자만 접근 가능합니다."),
    OUT_OF_LENGTH(BAD_REQUEST, ILLEGAL_ARGUMENT, "요청 글자의 범위는 2~20 입니다.");


    private final HttpStatus httpStatus;
    private final ErrorDefinition errorDefinition;
    private final String message;
}