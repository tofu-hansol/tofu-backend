package com.hansol.tofu.board.domain.dto;

import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.clubphoto.domain.ClubPhotoRequestDTO;
import com.hansol.tofu.member.domain.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;

public record BoardCreationRequestDTO(
        @NotBlank(message = "제목을 입력해주세요")
        String title,

        @NotBlank(message = "내용을 입력해주세요")
        String content,

        @RequestPart(value = "clubPhotoRequestDTOs", required = false)
        List<ClubPhotoRequestDTO> clubPhotoRequestDTOs
) {
    @Builder
    public BoardCreationRequestDTO {
    }

    public BoardEntity toEntity(MemberEntity member) {
        return BoardEntity.builder()
                .title(this.title)
                .content(this.content)
                .member(member)
                .build();
    }
}
