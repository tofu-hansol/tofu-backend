package com.hansol.tofu.board.domain.dto;

import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.member.domain.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record BoardCreationRequestDTO(
        @NotBlank(message = "제목을 입력해주세요")
        String title,

        @NotBlank(message = "내용을 입력해주세요")
        String content
) {
    @Builder
    public BoardCreationRequestDTO {
    }

    public BoardEntity toEntity(MemberEntity member, Long clubId) {
        return BoardEntity.builder()
                .title(this.title)
                .content(this.content)
                .member(member)
				.clubId(clubId)
                .build();
    }
}
