package com.hansol.tofu.clubmember.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record ClubMemberResponseDTO(
	Long id,
	Long memberId,
	String memberProfileUrl,
	String deptName,
	String memberName,
	String clubRole,
	String joinDate
) {
	@Builder
	@QueryProjection
	public ClubMemberResponseDTO {
	}
}
