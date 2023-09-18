package com.hansol.tofu.auth.email;

import com.hansol.tofu.auth.email.dto.EmailRequestDTO;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class EmailSenderServiceTest {

    private EmailSenderService sut;
    private JavaMailSender javaMailSender;
    private MimeMessage mimeMessage;


    @BeforeEach
    void setUp() {
        mimeMessage = new MimeMessage((Session)null);
        javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        sut = new EmailSenderServiceImpl(javaMailSender);
    }

    @Test
    void sendEmail_메일전송_요청시_전송에_성공한다() {
        var emailRequestDTO = EmailRequestDTO.builder()
                .to("mch@hansol.com")
                .subject("회원가입 인증요청")
                .content("인증번호는 아래와 같습니다.")
                .build();


        sut.sendEmail(emailRequestDTO);


        verify(javaMailSender).send(mimeMessage);
    }

}