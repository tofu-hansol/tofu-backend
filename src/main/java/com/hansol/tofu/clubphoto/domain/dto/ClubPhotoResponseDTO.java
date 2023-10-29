package com.hansol.tofu.clubphoto.domain.dto;

import java.time.ZonedDateTime;

import com.hansol.tofu.clubphoto.domain.ClubPhotoEntity;
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

	public static ClubPhotoResponseDTO of(ClubPhotoEntity clubPhoto) {
		return ClubPhotoResponseDTO.builder()
			.id(clubPhoto.getId())
			.imageUrl(clubPhoto.getImageUrl())
			.isMainPhoto(clubPhoto.getIsMainPhoto())
			.createdAt(clubPhoto.getCreatedAt())
			.build();
	}
}
