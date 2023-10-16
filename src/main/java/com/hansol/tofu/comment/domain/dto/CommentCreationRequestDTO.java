package com.hansol.tofu.comment.domain.dto;

import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.comment.domain.entity.CommentEntity;
import com.hansol.tofu.member.domain.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record CommentCreationRequestDTO(
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {
    @Builder
    public CommentCreationRequestDTO {
    }

    public CommentEntity toEntity(MemberEntity member, BoardEntity board) {
        return CommentEntity.builder()
                .content(this.content())
                .member(member)
                .board(board)
                .build();
    }
}
