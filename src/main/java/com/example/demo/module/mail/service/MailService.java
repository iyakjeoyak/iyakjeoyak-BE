package com.example.demo.module.mail.service;

import com.example.demo.global.memoryDb.RedisRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final RedisRepository repository;

    private Integer authNumber;

    @Value("${spring.data.redis.custom-expiration_time.mail}")
    private Long mailExpirationTime;

    public Integer joinForm(String email) throws MessagingException {
        setNum();
        String setFrom = "ddoly0106@bu.ac.kr"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                "IYJY-이약저약 을 방문해주셔서 감사합니다." +    //html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 입력해주세요"; //이메일 내용 삽입
        mailSend(setFrom, toMail, title, content);
        repository.setData(email, authNumber.toString(), mailExpirationTime);
        return authNumber;
    }

    private void mailSend(String setFrom, String toMail, String title, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        mimeMessageHelper.setFrom(setFrom);
        mimeMessageHelper.setTo(toMail);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(content, true);

        mailSender.send(mimeMessage);
    }

    private void setNum() {
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        authNumber = Integer.valueOf(str.toString());
    }

    public Boolean verifyMail(String email, String authCode) {
        boolean result = repository.getData(email).equals(authCode);
        if (result) {
            repository.deleteData(email);
            return true;
        }
        return false;
    }

}
