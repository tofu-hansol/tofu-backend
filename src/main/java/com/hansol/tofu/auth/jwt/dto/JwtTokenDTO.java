package com.hansol.tofu.auth.jwt.dto;

import lombok.Builder;

public record JwtTokenDTO(
	String accessToken,
	String refreshToken
) {
	@Builder
	public JwtTokenDTO {
	}
}
