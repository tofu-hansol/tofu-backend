package com.hansol.tofu.clubmember.domain.dto;

import lombok.Builder;

public record ClubJoinResponseDTO(
	String clubName,
	String clubRole,
	String joinDate
) {
	@Builder
	public ClubJoinResponseDTO {
	}
}
