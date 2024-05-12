package com.example.demo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ErrorResult {
    private int status;
    private String code;
    private String detail;
    private List<String> message;

    //모든 예외에서 사용가능
    public static ResponseEntity<ErrorResult> ofResponse(Exception e, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResult.builder()
                        .status(httpStatus.value())
                        .code(e.getClass().getSimpleName())
                        .message(List.of(e.getMessage()))
                        .build()
                );
    }

    public static ResponseEntity<ErrorResult> ofResponse(Exception e, List<String> messages, HttpStatus httpStatus) {
        e.printStackTrace();
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResult.builder()
                        .status(httpStatus.value())
                        .code(e.getClass().getSimpleName())
                        .message(messages)
                        .build()
                );
    }

    // 커스텀에서 사용시
    public static ResponseEntity<ErrorResult> ofResponse(ErrorCode e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResult.builder()
                        .status(e.getHttpStatus().value())
                        .code(e.getErrorDefinition().toString())
                        .detail(e.name())
                        .message(List.of(e.getMessage()))
                        .build()
                );
    }
}
