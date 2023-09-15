package com.hansol.tofu.club.domain.dto;

import com.hansol.tofu.club.enums.ClubRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "동호회 권한")
public record ClubAuthorization(
		@Schema(description = "동호회 ID")
		Long clubId,

		@Schema(description = "동호회 권한", example = "PRESIDENT")
		ClubRole clubRole
) {
	@Builder
	public ClubAuthorization {
	}
}