package com.hansol.tofu.comment.domain.dto;

import java.time.LocalDateTime;

import com.hansol.tofu.comment.domain.entity.CommentEntity;
import com.hansol.tofu.member.domain.MemberEntity;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record CommentResponseDTO(
	Long id,
	Long memberId,
	String memberName,
	String memberProfileImage,
	String deptName,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	String content
) {
	@Builder
	@QueryProjection
	public CommentResponseDTO {
	}

	public static CommentResponseDTO of(CommentEntity comment, MemberEntity member) {
		return CommentResponseDTO.builder()
			.id(comment.getId())
			.memberId(member.getId())
			.memberName(member.getName())
			.memberProfileImage(member.getProfileUrl())
			.deptName(member.getDept().getName())
			.createdAt(comment.getCreatedAt().toLocalDateTime())
			.updatedAt(comment.getUpdatedAt().toLocalDateTime())
			.content(comment.getContent())
			.build();
	}
}
