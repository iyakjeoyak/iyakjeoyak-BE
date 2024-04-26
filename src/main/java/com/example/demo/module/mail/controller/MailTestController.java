package com.example.demo.module.mail.controller;

import com.example.demo.module.mail.dto.payload.EmailPayload;
import com.example.demo.module.mail.dto.payload.EmailVerifyPayload;
import com.example.demo.module.mail.service.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
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
    public Integer joinMail(@RequestBody EmailPayload payload) throws MessagingException {
        return mailService.joinForm(payload.getEmail());
    }

    @PostMapping("/verify")
    public Boolean verifyMail(@RequestBody EmailVerifyPayload payload) {
        return mailService.verifyMail(payload.getEmail(), payload.getAuthCode());
    }

}
