package com.hansol.tofu.auth.email;

import com.hansol.tofu.auth.email.dto.VerificationToken;
import com.hansol.tofu.auth.email.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerificationTokenProvider {

    private final VerificationTokenRepository verificationTokenRepository;

    public Optional<VerificationToken> findVerificationTokenByEmail(String email) {
        return verificationTokenRepository.findById(email);
    }

    public Optional<VerificationToken> findVerificationTokenByCode(String code) {
        return verificationTokenRepository.findByCode(code);
    }

    public void createVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    public void deleteVerificationTokenBy(String email) {
        verificationTokenRepository.deleteById(email);
    }
}
