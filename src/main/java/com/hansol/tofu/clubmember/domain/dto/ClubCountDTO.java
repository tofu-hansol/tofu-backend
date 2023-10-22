package com.hansol.tofu.clubmember.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record ClubCountDTO(
	Long clubId,
	Long memberCount
) {
	@Builder
	@QueryProjection
	public ClubCountDTO {
	}
}
