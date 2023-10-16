package com.hansol.tofu.board.domain.dto;

import com.hansol.tofu.clubphoto.domain.ClubPhotoRequestDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

public record BoardEditRequestDTO(
        @NotBlank(message = "제목을 입력해주세요")
        String title,

        @NotBlank(message = "내용을 입력해주세요")
        String content,

        List<ClubPhotoRequestDTO> clubPhotoRequestDTOs
) {
    @Builder
    public BoardEditRequestDTO {
    }
}
