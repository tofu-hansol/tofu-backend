package com.hansol.tofu.board.domain.dto;

import java.time.ZonedDateTime;
import java.util.List;

import com.hansol.tofu.clubphoto.domain.dto.ClubPhotoResponseDTO;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

public record BoardResponseDTO(
	Long boardId,
	Long memberId,
	String memberProfileUrl,
	String deptName,
	String memberName,
	String title,
	String content,
	Long commentCount,
	ZonedDateTime createdAt,
	ZonedDateTime updatedAt,
	List<ClubPhotoResponseDTO> photoList
) {
	@Builder
	@QueryProjection
	public BoardResponseDTO {
	}
}
