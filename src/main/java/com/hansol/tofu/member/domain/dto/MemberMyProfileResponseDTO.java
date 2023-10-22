package com.hansol.tofu.member.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record MemberMyProfileResponseDTO(
	String email,
	String name,
	String profileImageUrl,
	String companyName,
	String deptName,
	String positionName,
	String mbti
) {
	@QueryProjection
	@Builder
	public MemberMyProfileResponseDTO {
	}
}
