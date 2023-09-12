package com.hansol.tofu.auth;

import com.hansol.tofu.auth.domain.dto.SignupRequestDTO;
import com.hansol.tofu.auth.jwt.JwtTokenProvider;
import com.hansol.tofu.auth.jwt.dto.JwtTokenDTO;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.member.MemberService;
import com.hansol.tofu.member.domain.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.hansol.tofu.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	public JwtTokenDTO refresh(String refreshToken) {

		String email = jwtTokenProvider.getEmailByToken(refreshToken);

		if (jwtTokenProvider.isTokenExpired(refreshToken)) {
			throw new BaseException(INVALID_TOKEN);
		}

		var memberEntity = memberService.findMemberBy(email)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var refreshTokenEntity = jwtTokenProvider.findRefreshTokenBy(email)
			.orElseThrow(() -> new BaseException(NOT_FOUND_TOKEN));
		jwtTokenProvider.deleteRefreshTokenBy(refreshTokenEntity.getEmail());

		return jwtTokenProvider.createToken(memberEntity.getEmail(), memberEntity.getUserRole());
	}

	public Long signup(SignupRequestDTO signupRequestDTO) {
		var memberRequestDTO = MemberRequestDTO.builder()
				.email(signupRequestDTO.email())
				.name(signupRequestDTO.name())
				.password(passwordEncoder.encode(signupRequestDTO.password()))
				.mbti(signupRequestDTO.mbti())
				.deptId(signupRequestDTO.deptId())
				.build();

		return memberService.saveMember(memberRequestDTO);
	}
}
