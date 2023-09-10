package com.hansol.tofu.auth;

import static com.hansol.tofu.error.ErrorCode.*;
import static com.hansol.tofu.member.enums.MemberStatus.*;

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

	// private final KakaoAuthService kakaoAuthService;
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;

	// public LoginResponseDTO kakaoLogin(String kakaoAccessToken) {
	//     var kakaoResponse = kakaoAuthService.authenticate(kakaoAccessToken);
	//
	//     if (kakaoResponse == null) {
	//         throw new BaseException(ErrorCode.FAILED_KAKAO_AUTH);
	//     }
	//
	//     var email = kakaoResponse.kakaoAccountDTO().email();
	//     var findMember = memberRepository.findMemberByEmailAndMemberStatus(email, MemberStatus.ACTIVATE);
	//
	//     if (findMember.isEmpty()) {
	//         var createMember = MemberEntity.builder()
	//                 .name(kakaoResponse.kakaoUserPropertiesDTO().nickname())
	//                 .email(kakaoResponse.kakaoAccountDTO().email())
	//                 .password(kakaoAuthService.getEncryptedPassword(kakaoResponse.id()))
	//                 .career(0)
	//                 .userRole(UserRole.ROLE_USER)
	//                 .build();
	//
	//         var savedMember = memberRepository.save(createMember);
	//
	// 		return createLoginResponse(savedMember, UserRole.ROLE_USER);
	//     }
	//
	// 	return createLoginResponse(findMember.get(), findMember.get().getUserRole());
	// }

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

	private LoginResponseDTO createLoginResponse(MemberEntity memberEntity, UserRole userRole) {
		var jwtTokenDTO = jwtTokenProvider.createToken(memberEntity.getEmail(), userRole);

		return LoginResponseDTO.builder()
			.memberId(memberEntity.getId())
			.accessToken(jwtTokenDTO.accessToken())
			.refreshToken(jwtTokenDTO.refreshToken())
			.build();
	}
}
