package com.hansol.tofu.auth.controller;

import com.hansol.tofu.auth.domain.dto.SignupRequestDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

	@Operation(summary = "회원가입 API", responses = {
		@ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "400", description = "요청값 에러", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
		@ApiResponse(responseCode = "409", description = "존재하는 회원", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@PostMapping("/signup")
	public BaseHttpResponse<Long> signup(@RequestBody @Valid SignupRequestDTO signupRequestDTO) {
		return BaseHttpResponse.success(authService.signup(signupRequestDTO));
	}

	@Operation(summary = "Refresh Token 재발급 API", responses = {
		@ApiResponse(responseCode = "200", description = "Refresh Token 재발급 성공", content = @Content(schema = @Schema(implementation = JwtTokenDTO.class))),
		@ApiResponse(responseCode = "401", description = "Refresh Token 유효하지 않음", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@PostMapping("/refresh")
	public BaseHttpResponse<JwtTokenDTO> refresh(@RequestHeader("RefreshToken") String refreshToken) {
		var loginResponseDTO = authService.refresh(refreshToken);
		return BaseHttpResponse.success(loginResponseDTO);
	}
}
