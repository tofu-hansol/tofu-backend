package com.hansol.tofu.auth.filter.jwt.dto;

import lombok.Builder;

public record JwtTokenDTO(
	String accessToken,
	String refreshToken
) {
	@Builder
	public JwtTokenDTO {
	}
}
