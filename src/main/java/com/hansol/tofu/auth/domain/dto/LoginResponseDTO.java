package com.hansol.tofu.auth.domain.dto;

import lombok.Builder;

public record LoginResponseDTO(
	Long memberId,
	String accessToken,
	String refreshToken
) {
	@Builder
	public LoginResponseDTO {
	}
}
