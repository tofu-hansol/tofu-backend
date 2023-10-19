package com.hansol.tofu.clubmember.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record ClubJoinResponseDTO(
	String clubName,
	String clubRole,
	String joinDate
) {
	@Builder
	@QueryProjection
	public ClubJoinResponseDTO {
	}
}
