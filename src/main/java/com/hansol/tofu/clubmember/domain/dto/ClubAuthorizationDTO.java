package com.hansol.tofu.clubmember.domain.dto;

import com.hansol.tofu.club.enums.ClubRole;

import lombok.Builder;

public record ClubAuthorizationDTO(
	Long clubId,
	String clubName,
	ClubRole clubRole
) {
	@Builder
	public ClubAuthorizationDTO {
	}
}
