package com.hansol.tofu.member.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record MemberProfileResponseDTO(
	String name,
	String profileImageUrl,
	String companyName,
	String deptName,
	String positionName
) {
	@Builder
	@QueryProjection
	public MemberProfileResponseDTO {
	}
}
