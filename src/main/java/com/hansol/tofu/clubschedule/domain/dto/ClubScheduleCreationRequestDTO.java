package com.hansol.tofu.clubschedule.domain.dto;

import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record ClubScheduleCreationRequestDTO(
        @NotBlank(message = "제목을 입력해주세요")
        String title,
        String content,
        @Future(message = "현재 시간 이후의 일정만 등록할 수 있습니다")
        LocalDateTime eventAt,
		Double latitude,
		Double longitude
) {
    @Builder
    public ClubScheduleCreationRequestDTO {
    }

    public ClubScheduleEntity toEntity(ClubEntity club) {
        return ClubScheduleEntity.builder()
                .title(this.title)
                .placeName(this.content)
                .eventAt(ZonedDateTime.of(this.eventAt(), ZoneId.of("Asia/Seoul")))
				.latitude(this.latitude)
				.longitude(this.longitude)
                .club(club)
                .build();
    }
}
