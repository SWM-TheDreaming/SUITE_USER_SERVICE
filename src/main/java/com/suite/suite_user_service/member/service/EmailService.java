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
        message.setText(mailTemplate(authCode), "utf-8", "html");

        return message;
    }

    private String mailTemplate(String authCode) {
        return
                "<html>\n" +
                        "<body>\n" +
                        "\n" +
                        "<center style=\"min-width:600px;width:100%\">\n" +
                        "\t\t\t<table align=\"center\" class=\"m_-8503328126709798540container\" style=\"Margin:0 auto;background:#f7f7f8;border-collapse:collapse;border-spacing:0;float:none;margin:0 auto;padding:0;text-align:center;vertical-align:top;width:600px\">\n" +
                        "\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t<td style=\"Margin:0;border-collapse:collapse!important;color:#0a0a0a;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\">\n" +
                        "\t\t\t\t\t\t<table class=\"m_-8503328126709798540visible-xs\" style=\"background:#f7f7f8;border-collapse:collapse;border-spacing:0;display:none;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t<td height=\"32\" style=\"Margin:0;border-collapse:collapse!important;color:#0a0a0a;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:32px;font-weight:400;line-height:32px;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\"></td>\n" +
                        "\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t</table>\n" +
                        "\n" +
                        "\t\t\t\t\t\t<table class=\"m_-8503328126709798540hidden-xs\" style=\"background:#f7f7f8;border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t</table>\n" +
                        "\n" +
                        "\t\t\t\t\t\t<table style=\"background:#f7f7f8;border-collapse:collapse;border-spacing:0;display:table;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t<th class=\"m_-8503328126709798540small-12 m_-8503328126709798540columns m_-8503328126709798540first m_-8503328126709798540last\" style=\"Margin:0 auto;color:#0a0a0a;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0 auto;padding:0!important;padding-bottom:16px;padding-right:24px;text-align:left;width:276px\">\n" +
                        "\t\t\t\t\t\t\t\t\t<table style=\"border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<tr style=\"text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<th class=\"m_-8503328126709798540logo\" style=\"Margin:0;color:#0a0a0a;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0;padding:0;padding-left:40px;text-align:left\"></th>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t\t\t</th>\n" +
                        "\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t</table>\n" +
                        "                        \n" +
                        "\t\t\t\t\t\t<table class=\"m_-8503328126709798540visible-xs\" style=\"background:#f7f7f8;border-collapse:collapse;border-spacing:0;display:none;padding:0;width:100%\">\n" +
                        "\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t<tr style=\"padding:0\">\n" +
                        "\t\t\t\t\t\t\t\t\t<td height=\"24\" style=\"Margin:0;border-collapse:collapse!important;color:#0a0a0a;font-size:24px;line-height:24px;padding:0\"></td>\n" +
                        "\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t<table class=\"m_-8503328126709798540hidden-xs\" style=\"background:#f7f7f8;border-collapse:collapse;border-spacing:0;padding:0;width:100%\">\n" +
                        "\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t</tbody>\n" +
                        "\t\t\t</table>\n" +
                        "\n" +
                        "\t\t\t<table align=\"center\" class=\"m_-8503328126709798540container\" style=\"Margin:0 auto;background:#fefefe;border-collapse:collapse;border-radius:3px!important;border-spacing:0;float:none;margin:0 auto;padding:0;text-align:center;vertical-align:top;width:600px\">\n" +
                        "\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t<td style=\"Margin:0;border-collapse:collapse!important;color:#050953;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\">\n" +
                        "\t\t\t\t\t\t<table style=\"border-collapse:collapse;border-spacing:0;display:table;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t<th class=\"m_-8503328126709798540content m_-8503328126709798540small-12 m_-8503328126709798540large-12 m_-8503328126709798540columns m_-8503328126709798540first m_-8503328126709798540last\" style=\"Margin:0 auto;color:#0a0a0a;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0 auto;padding:0;padding-bottom:16px;padding-left:40px;padding-right:40px;text-align:left;width:576px\">\n" +
                        "\t\t\t\t\t\t\t\t\t<table style=\"border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<th style=\"Margin:0;color:#050953;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0;padding:0;text-align:left\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<table class=\"m_-8503328126709798540visible-xs\" style=\"border-collapse:collapse;border-spacing:0;display:none;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"24\" style=\"Margin:0;border-collapse:collapse!important;color:#050953;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:24px;font-weight:400;line-height:24px;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\"></td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<table class=\"m_-8503328126709798540hidden-xs\" style=\"border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"40\" style=\"Margin:0;border-collapse:collapse!important;color:#050953;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:40px;font-weight:400;line-height:40px;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\"></td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<p class=\"m_-8503328126709798540h2\" style=\"Margin:0;Margin-bottom:0;color:#000000;font-family:Inter,Inter,Roboto,Helvetica,sans-serif;font-size:30px;font-weight:500!important;line-height:1.3;margin:0;margin-bottom:0;padding:0;text-align:left\">진심을 보증합니다. Suite</p>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<table style=\"border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"32\" style=\"Margin:0;border-collapse:collapse!important;color:#050953;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:32px;font-weight:400;line-height:32px;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\"></td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<p class=\"m_-8503328126709798540lead\" style=\"Margin:0;Margin-bottom:0;color:#000000;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400!important;line-height:1.3;margin:0;margin-bottom:0;padding:0;text-align:left\">본인 확인을 위해 아래 인증 코드를 입력해주세요.</p>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<table style=\"border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"32\" style=\"Margin:0;border-collapse:collapse!important;color:#0a0a0a;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:32px;font-weight:400;line-height:32px;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\"></td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t\t  <table style=\"border-collapse:separate;border-spacing:0;display:table;padding:0;text-align:left;vertical-align:top;width:auto\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "                              <td style=\"Margin:0;background:#050953;border-collapse:collapse!important;border-radius:5px;color:#fefefe;font-family:Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0;padding:16px;text-align:left;vertical-align:top;word-wrap:break-word\">\n" +
                        "                               <p class=\"m_-8503328126709798540h2\" style=\"Margin:0;Margin-bottom:0;color:#ffffff;font-family:Inter,Inter,Roboto,Helvetica,sans-serif;font-size:24px;font-weight:500!important;letter-spacing:2px;line-height:1.3;margin:0;margin-bottom:0;padding:0;text-align:left\">" + authCode + "\n" +
                        "                                </p>\n" +
                        "                              </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                          </table>\n" +
                        "\t\t\t\t\t\t\t\t\n" +
                        "<table style=\"border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "<tbody>\n" +
                        "<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "<td height=\"32\" style=\"margin:0;border-collapse:collapse!important;color:#050953;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:32px;font-weight:400;line-height:24px;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\"></td>\n" +
                        "</tr>\n" +
                        "</tbody>\n" +
                        "</table>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<p class=\"m_-8503328126709798540lead\" style=\"Margin:0;Margin-bottom:0;color:#000000;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400!important;line-height:1.3;margin:0;margin-bottom:0;padding:0;text-align:left\">저희 Suite 서비스에 가입해주셔서 감사합니다. 진심이 담긴 스터디에 가입하여 꼭 성공하시길 바랍니다!</p>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<table style=\"border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"24\" style=\"Margin:0;border-collapse:collapse!important;color:#0a0a0a;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:24px;font-weight:400;line-height:24px;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\"></td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</th>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<th style=\"Margin:0;color:#0a0a0a;font-family:Inter,Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0;padding:0!important;text-align:left;width:0\"></th>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t\t\t</th>\n" +
                        "\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t</tbody>\n" +
                        "\t\t\t</table>\n" +
                        "\t\t\t\n" +
                        "          <table style=\"border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "  <tbody>\n" +
                        "    <tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "      <td height=\"40\" style=\"Margin:0;border-collapse:collapse!important;color:#0a0a0a;font-family:Roboto,Helvetica,sans-serif;font-size:40px;font-weight:400;line-height:40px;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\">\n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody>\n" +
                        "</table>\n" +
                        "<table align=\"center\" class=\"m_-8503328126709798540container\" style=\"Margin:0 auto;background:#fefefe;background:#f7f7f8;border-collapse:collapse;border-spacing:0;margin:0 auto;padding:0;text-align:inherit;vertical-align:top;width:600px\">\n" +
                        "  <tbody>\n" +
                        "    <tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "      <td style=\"Margin:0;border-collapse:collapse!important;color:#0a0a0a;font-family:Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\">\n" +
                        "        <table style=\"border-collapse:collapse;border-spacing:0;display:table;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "          <tbody>\n" +
                        "            <tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "              <th class=\"m_-8503328126709798540small-12 m_-8503328126709798540large-12 m_-8503328126709798540columns m_-8503328126709798540first m_-8503328126709798540last\" style=\"Margin:0 auto;color:#0a0a0a;font-family:Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0 auto;padding:0;padding-bottom:16px;padding-left:0!important;padding-right:0!important;text-align:left;width:576px\">\n" +
                        "                <table style=\"border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "                  <tbody>\n" +
                        "                    <tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "                      <th style=\"Margin:0;color:#0a0a0a;font-family:Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0;padding:0;text-align:left\">\n" +
                        "                       \n" +
                        "                        <p style=\"Margin:0;Margin-bottom:0;color:#b5b6b9;font-family:Roboto,Helvetica,sans-serif;font-size:12px;font-weight:400;line-height:1.3;margin:0;margin-bottom:0;padding:0;text-align:center\"><br/><a href=\"https://suitestudy.notion.site/suitestudy/Suite-d6783cf456c64703be5a41dc26e783c5\">개인정보 처리 방침</a>&nbsp;&nbsp; 문의 : <a style=\"Margin:0;color:#b5b6b9;font-family:Roboto,Helvetica,sans-serif;font-size:12px;font-weight:400;line-height:1.3;margin:0;padding:0;text-align:left;text-decoration:underline\" href=\"mailto:zxz4641@gmail.com\" target=\"_blank\">zxz4641@gmail.com</a>\n" +
                        "                        </p>\n" +
                        "                      \n" +
                        "                        <table style=\"background:#f7f7f8;border-collapse:collapse;border-spacing:0;padding:0;text-align:left;vertical-align:top;width:100%\">\n" +
                        "                          <tbody>\n" +
                        "                            <tr style=\"padding:0;text-align:left;vertical-align:top\">\n" +
                        "                              <td height=\"32\" style=\"Margin:0;border-collapse:collapse!important;color:#0a0a0a;font-family:Roboto,Helvetica,sans-serif;font-size:32px;font-weight:400;line-height:32px;margin:0;padding:0;text-align:left;vertical-align:top;word-wrap:break-word\">\n" +
                        "                              </td>\n" +
                        "                            </tr>\n" +
                        "                          </tbody>\n" +
                        "                        </table>\n" +
                        "                      </th>\n" +
                        "                      <th style=\"Margin:0;color:#0a0a0a;font-family:Roboto,Helvetica,sans-serif;font-size:16px;font-weight:400;line-height:1.3;margin:0;padding:0!important;text-align:left;width:0\">\n" +
                        "                      </th>\n" +
                        "                    </tr>\n" +
                        "                  </tbody>\n" +
                        "                </table>\n" +
                        "              </th>\n" +
                        "            </tr>\n" +
                        "          </tbody>\n" +
                        "        </table>\n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody>\n" +
                        "</table>\n" +
                        "\n" +
                        "            \n" +
                        "            </center>\n" +
                        "</body>\n" +
                        "</html>";
    }

}
