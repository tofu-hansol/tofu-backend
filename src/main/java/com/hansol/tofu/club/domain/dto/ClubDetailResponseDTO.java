package com.hansol.tofu.club.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record ClubDetailResponseDTO(
	Long clubId,
	String name,
	String clubProfileUrl,
	String clubBackgroundUrl,
	String description,
	int fee,
	String accountNumber,
	long memberCount,
	long boardCount
) {

	@Builder
	@QueryProjection
	public ClubDetailResponseDTO {
	}

}
