package com.hansol.tofu.clubmember.domain.dto;

import com.hansol.tofu.clubmember.enums.ClubJoinStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record ClubMemberResponseDTO(
	Long id,
	Long memberId,
	String memberProfileUrl,
	String deptName,
	String memberName,
	String clubRole,
	String joinDate,
	ClubJoinStatus clubJoinStatus
) {
	@Builder
	@QueryProjection
	public ClubMemberResponseDTO {
	}
}
