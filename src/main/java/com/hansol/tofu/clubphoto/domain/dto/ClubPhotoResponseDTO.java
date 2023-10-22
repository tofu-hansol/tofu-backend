package com.hansol.tofu.clubphoto.domain.dto;

import java.time.ZonedDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record ClubPhotoResponseDTO(
		Long id,
		String imageUrl,
		boolean isMainPhoto,
		ZonedDateTime createdAt
) {
	@Builder
	@QueryProjection
	public ClubPhotoResponseDTO {
	}
}
