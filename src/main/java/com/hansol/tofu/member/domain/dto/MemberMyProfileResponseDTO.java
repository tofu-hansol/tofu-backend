package com.hansol.tofu.member.domain.dto;

import java.util.List;

import com.hansol.tofu.clubmember.domain.dto.ClubJoinResponseDTO;
import com.querydsl.core.annotations.QueryProjection;

public record MemberMyProfileResponseDTO(
	String email,
	String name,
	String profileImageUrl,
	String companyName,
	String deptName,
	String positionName,
	String mbti,
	List<ClubJoinResponseDTO> clubJoinInfo
) {
	@QueryProjection
	public MemberMyProfileResponseDTO {
	}
}
