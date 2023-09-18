package com.hansol.tofu.auth.email.repository;

import com.hansol.tofu.auth.email.dto.VerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, String> {

    Optional<VerificationToken> findByCode(String code);
}
