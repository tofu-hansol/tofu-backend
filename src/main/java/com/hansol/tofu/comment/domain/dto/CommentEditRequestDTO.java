package com.hansol.tofu.comment.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record CommentEditRequestDTO(
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {
    @Builder
    public CommentEditRequestDTO {
    }
}
