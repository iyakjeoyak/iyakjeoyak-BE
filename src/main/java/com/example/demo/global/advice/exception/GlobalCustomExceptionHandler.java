package com.example.demo.global.advice.exception;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Order(1)
@Slf4j
public class GlobalCustomExceptionHandler {


    //커스텀 익셉션 사용 예
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResult> validException(CustomException e) {
        return ErrorResult.ofResponse(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> validException(MethodArgumentNotValidException e) {
        List<String> answer = getFieldErrorMessage(e);
        return ErrorResult.ofResponse(e, answer, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResult> nullPointException(NullPointerException e) {
        return ErrorResult.ofResponse(e, HttpStatus.valueOf(500));
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResult> handleCustomException(NoSuchElementException e) {
        return ErrorResult.ofResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> handleCustomException(IllegalArgumentException e) {
        return ErrorResult.ofResponse(e, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResult> handleCustomException(IOException e) {
        return ErrorResult.ofResponse(e, HttpStatus.BAD_REQUEST);
    }


    private static List<String> getFieldErrorMessage(MethodArgumentNotValidException e) {
        List<String> answer = new ArrayList<>();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String error = fieldError.getField() + "=" + fieldError.getDefaultMessage();
            answer.add(error);
        }
        return answer;
    }
}