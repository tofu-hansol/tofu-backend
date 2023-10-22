package com.hansol.tofu.club.domain.dto;

import java.time.ZonedDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record ClubResponseDTO(
	Long clubId,
	String profileImageUrl,
	String clubName,
	long memberCount,
	ZonedDateTime createdDate,
	String clubDescription
) {

	@Builder
	@QueryProjection
	public ClubResponseDTO {
	}
}
