package com.example.demo.module.mail.controller;

import com.example.demo.module.mail.dto.payload.EmailPayload;
import com.example.demo.module.mail.dto.payload.EmailVerifyPayload;
import com.example.demo.module.mail.service.MailService;
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
@RequestMapping("/mail")
@Tag(name = "이메일 인증 테스트용")
public class MailTestController {
    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<Integer> joinMail(@RequestBody EmailPayload payload) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.joinForm(payload.getEmail()));
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyMail(@RequestBody EmailVerifyPayload payload) {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.verifyMail(payload.getEmail(), payload.getAuthCode()));
    }

}
