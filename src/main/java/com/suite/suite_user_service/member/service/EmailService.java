package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.EmailDto;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String setFrom;
    private final JavaMailSender emailSender;

    public EmailDto sendEmailCode(EmailDto emailDto) {
        emailDto.setCode(createCode());
        try {
            MimeMessage emailForm = createEmailForm(emailDto.getEmail(), emailDto.getCode());
            emailSender.send(emailForm);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(StatusCode.FAILED_REQUEST);
        }

        return emailDto;
    }

    private String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);
            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }

        //return key.toString();
        return "49723621";
    }

    //메일 양식 작성
    private MimeMessage createEmailForm(String email, String authCode) throws MessagingException, UnsupportedEncodingException {

        createCode(); //인증 코드 생성
        String toEmail = email; //받는 사람
        String title = "Suite 회원가입 인증 번호"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText("인증 코드 : " + authCode, "utf-8", "html");

        return message;
    }

}
