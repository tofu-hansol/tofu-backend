package com.hansol.tofu.board.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record BoardEditRequestDTO(
        @NotBlank(message = "제목을 입력해주세요")
        String title,

        @NotBlank(message = "내용을 입력해주세요")
        String content
) {
    @Builder
    public BoardEditRequestDTO {
    }
}
