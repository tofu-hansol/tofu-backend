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
        LocalDateTime eventAt
) {
    @Builder
    public ClubScheduleCreationRequestDTO {
    }

    public ClubScheduleEntity toEntity(ClubScheduleCreationRequestDTO schedule, ClubEntity club) {
        return ClubScheduleEntity.builder()
                .title(schedule.title())
                .content(schedule.content())
                .eventAt(ZonedDateTime.of(schedule.eventAt(), ZoneId.of("Asia/Seoul")))
                .club(club)
                .build();
    }
}
