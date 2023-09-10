package com.hansol.tofu.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.auth.AuthService;
import com.hansol.tofu.auth.jwt.dto.JwtTokenDTO;
import com.hansol.tofu.global.BaseHttpResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	// private final KakaoOAuthProperties kakaoOAuthProperties;

	// @GetMapping("/kakao")
	// @Operation(summary = "카카오 로그인 페이지 요청 API")
	// public void requestKakaoLoginPage(HttpServletResponse response) {
	//
	// 	StringBuilder url = new StringBuilder("https://kauth.kakao.com/oauth/authorize?");
	// 	url.append("client_id=").append(kakaoOAuthProperties.getClientId());
	// 	url.append("&redirect_uri=").append(kakaoOAuthProperties.getCallbackUrl());
	// 	url.append("&response_type=code");
	//
	// 	try {
	// 		response.sendRedirect(url.toString());
	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 	}
	// }
	//
	// @GetMapping("/login")
	// @Operation(summary = "카카오 로그인 API")
	// public BaseHttpResponse<LoginResponseDTO> login(@RequestParam String kakaoAccessToken) {
	// 	var loginResponseDTO = authService.kakaoLogin(kakaoAccessToken);
	// 	return BaseHttpResponse.success(loginResponseDTO);
	// }

	@PostMapping("/refresh")
	@Operation(summary = "Refresh Token 재발행 API")
	public BaseHttpResponse<JwtTokenDTO> refresh(@RequestHeader("RefreshToken") String refreshToken) {
		var loginResponseDTO = authService.refresh(refreshToken);
		return BaseHttpResponse.success(loginResponseDTO);
	}
}
