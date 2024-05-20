package com.example.demo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.example.demo.global.exception.ErrorDefinition.*;
import static com.example.demo.global.exception.ErrorDefinition.NOT_FOUND;
import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // DB 조회 실패 에러 코드
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "리뷰 정보를 찾을 수 없습니다."),
    MEDICINE_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "영양제 정보를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "이미지 정보를 찾을 수 없습니다."),
    HASHTAG_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "해쉬태그 정보를 찾을 수 없습니다."),
    BOOKMARK_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "북마크 정보를 찾을 수 없습니다."),
    DECLARATION_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "신고 정보를 찾을 수 없습니다."),
    PHARMACY_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "약국 정보를 찾을 수 없습니다."),
    STORAGE_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "영양제 저장 내역이 없습니다."),
    AUTH_CORD_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "인증코드가 발송되지 않은 이메일입니다."),
    ROLE_NOT_FOUND(BAD_REQUEST, NOT_FOUND, "유저 권한을 찾을 수 없습니다."),


    // 로직 실패 에러코드
    REVIEW_DUPLICATION(BAD_REQUEST, ILLEGAL_ARGUMENT, "이미 후기를 작성한 영양제 입니다."),
    DECLARATION_DUPLICATION(BAD_REQUEST, ILLEGAL_ARGUMENT, "이미 신고한 리뷰입니다."),
    ACCESS_BLOCKED(BAD_REQUEST, ILLEGAL_ARGUMENT, "작성자만 접근 가능합니다."),
    OUT_OF_LENGTH(BAD_REQUEST, ILLEGAL_ARGUMENT, "요청 글자의 범위는 2~20 입니다."),
    PW_NOT_MATCH(BAD_REQUEST, ILLEGAL_ARGUMENT, "비밀번호가 일치하지 않습니다."),
    MAIL_NOT_VERIFY(BAD_REQUEST, ILLEGAL_ARGUMENT, "메일 인증번호가 일치하지 않습니다."),
    PW_CONFIRM_FAIL(BAD_REQUEST, ILLEGAL_ARGUMENT, "비밀번호 확인이 일치하지 않습니다."),
    PHARMACY_DUPLICATION(BAD_REQUEST, ILLEGAL_ARGUMENT, "이미 등록된 약국 정보입니다."),

    //JWT 실패 코드
    JWT_TIME_EXP(UNAUTHORIZED, SECURE, "토큰 시간이 만료되었습니다."),
    JWT_INVALID(FORBIDDEN, SECURE, "유효하지 않은 토큰이 입력되었습니다."),
    AUTHENTICATION_BLOCKED(FORBIDDEN, SECURE, "로그인 후 사용 가능한 서비스 입니다."),
    JWT_TYPE_ERROR(FORBIDDEN, SECURE, "토큰 타입이 잘못되었습니다.");



    private final HttpStatus httpStatus;
    private final ErrorDefinition errorDefinition;
    private final String message;
}