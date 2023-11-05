package com.hansol.tofu.applicant.domain.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;
import com.hansol.tofu.clubschedule.enums.ClubScheduleStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record ClubApplicationResponseDTO(
	Long id,
	String title,
	LocalDateTime eventAt,
	ClubScheduleStatus clubScheduleStatus,
	String placeName,
	Double latitude,
	Double longitude,
	int applicants
) {
	@Builder
	@QueryProjection
	public ClubApplicationResponseDTO {
	}

	public static ClubApplicationResponseDTO of(
		ClubScheduleEntity clubScheduleEntity,
		int applicants
	) {
		return new ClubApplicationResponseDTO(
			clubScheduleEntity.getId(),
			clubScheduleEntity.getTitle(),
			clubScheduleEntity.getEventAt().withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime(),
			clubScheduleEntity.getClubScheduleStatus(),
			clubScheduleEntity.getPlaceName(),
			clubScheduleEntity.getLatitude(),
			clubScheduleEntity.getLongitude(),
			applicants
		);
	}
}
