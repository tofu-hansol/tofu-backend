package com.hansol.tofu.clubschedule.domain.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

public record ClubScheduleEditRequestDTO(
        @NotBlank(message = "제목을 입력해주세요")
        String title,
        String placeName,
        @Future(message = "현재 시간 이후의 일정만 등록할 수 있습니다")
        LocalDateTime eventAt,
		Double latitude,
		Double longitude
) {
    @Builder
    public ClubScheduleEditRequestDTO {
    }
}
