package com.hansol.tofu.auth;

import static com.hansol.tofu.error.ErrorCode.*;
import static com.hansol.tofu.member.enums.MemberStatus.*;

import com.hansol.tofu.auth.domain.dto.SignupRequestDTO;
import org.springframework.stereotype.Service;

import com.hansol.tofu.auth.domain.dto.LoginResponseDTO;
import com.hansol.tofu.auth.jwt.JwtTokenProvider;
import com.hansol.tofu.auth.jwt.dto.JwtTokenDTO;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.enums.UserRole;
import com.hansol.tofu.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;

	public JwtTokenDTO refresh(String refreshToken) {

		String email = jwtTokenProvider.getEmailByToken(refreshToken);

		if (jwtTokenProvider.isTokenExpired(refreshToken)) {
			throw new BaseException(INVALID_TOKEN);
		}

		var memberEntity = memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var refreshTokenEntity = jwtTokenProvider.findRefreshTokenBy(email)
			.orElseThrow(() -> new BaseException(NOT_FOUND_TOKEN));
		jwtTokenProvider.deleteRefreshTokenBy(refreshTokenEntity.getEmail());

		return jwtTokenProvider.createToken(memberEntity.getEmail(), memberEntity.getUserRole());
	}

	public void signup(SignupRequestDTO signupRequestDTO) {

	}
}
