package com.hansol.tofu.auth;

import com.hansol.tofu.auth.email.EmailSenderService;
import com.hansol.tofu.auth.email.VerificationTokenProvider;
import com.hansol.tofu.auth.email.dto.EmailRequestDTO;
import com.hansol.tofu.auth.email.dto.VerificationToken;
import com.hansol.tofu.error.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.SecureRandom;

import static com.hansol.tofu.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailSenderService emailSenderService;
    private final VerificationTokenProvider verificationTokenProvider;

    private final HttpServletRequest httpServletRequest;

    public void verifyEmail(String email) {

        String code = createCode();
        String baseUrl = ServletUriComponentsBuilder.fromContextPath(httpServletRequest).build().toUriString();
        String verifyUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/auth/verify-email")
                .queryParam("code", code).toUriString();

        var emailRequestDTO = EmailRequestDTO.builder()
                .to(email)
                .subject("[두부한모] 회원가입 인증 메일입니다.")
                .content("회원가입 인증 메일입니다.\n\n" + "다음 링크를 클릭해주세요. : " +
                        verifyUrl)
                .build();

        saveVerificationToken(emailRequestDTO.to(), code);
        sendVerificationEmail(emailRequestDTO);
    }

    public String getEmailBy(String code) {
        return verificationTokenProvider.findVerificationTokenByCode(code)
                .orElseThrow(() -> new BaseException(NOT_FOUND_TOKEN))
                .getEmail();
    }


    private void saveVerificationToken(String email, String token) {
        var verificationToken = VerificationToken.builder()
                .email(email)
                .code(token)
                .build();
        verificationTokenProvider.createVerificationToken(verificationToken);
    }

    private void sendVerificationEmail(EmailRequestDTO emailRequestDTO) {
        emailSenderService.sendEmail(emailRequestDTO);
    }

    private String createCode() {
        SecureRandom random = new SecureRandom();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0 -> key.append((char) ((int) random.nextInt(26) + 97));
                case 1 -> key.append((char) ((int) random.nextInt(26) + 65));
                default -> key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

}
