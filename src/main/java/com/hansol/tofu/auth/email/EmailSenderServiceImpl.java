package com.hansol.tofu.auth.email;

import com.hansol.tofu.auth.email.dto.EmailRequestDTO;
import com.hansol.tofu.error.BaseException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.hansol.tofu.error.ErrorCode.FAILED_TO_SEND_EMAIL;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Override
    @Async
    public void sendEmail(EmailRequestDTO emailRequestDTO) {
        var mimeMessage = javaMailSender.createMimeMessage();

        try {
            var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom("no-reply@hansol.com");
            mimeMessageHelper.setTo(emailRequestDTO.to());
            mimeMessageHelper.setSubject(emailRequestDTO.subject());
            mimeMessageHelper.setText(emailRequestDTO.content());
        } catch (MessagingException e) {
            throw new BaseException(FAILED_TO_SEND_EMAIL);
        }

        javaMailSender.send(mimeMessage);
    }



}
