package com.example.demo.module.mail.controller;

import com.example.demo.module.mail.dto.payload.EmailPayload;
import com.example.demo.module.mail.dto.payload.EmailVerifyPayload;
import com.example.demo.module.mail.service.MailService;
import com.example.demo.module.review.dto.result.ReviewResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mails")
@Tag(name = "이메일 인증 기능")
public class MailTestController {
    private final MailService mailService;

    @PostMapping("/send/verify")
    @Operation(summary = "이메일 체크 메일 발송", description = "회원 가입시 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Integer> joinMail(@RequestBody EmailPayload payload) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.joinForm(payload.getEmail()));
    }

    @PostMapping("/verify")
    @Operation(summary = "이메일 인증코드 검증", description = "인증 코드 검증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Boolean> verifyMail(@RequestBody EmailVerifyPayload payload) {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.verifyMail(payload.getEmail(), payload.getAuthCode()));
    }

}
