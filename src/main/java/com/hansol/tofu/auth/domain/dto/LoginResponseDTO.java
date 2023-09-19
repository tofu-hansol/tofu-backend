package com.hansol.tofu.auth.domain.dto;

import java.util.Map;

import com.hansol.tofu.club.domain.dto.ClubAuthorizationDTO;

import lombok.Builder;

public record LoginResponseDTO(
	Long memberId,
	String accessToken,
	String refreshToken,
	Map<Long, ClubAuthorizationDTO> clubAuthorizationDTO
) {
	@Builder
	public LoginResponseDTO {
	}
}
