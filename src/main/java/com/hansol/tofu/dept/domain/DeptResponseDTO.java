package com.hansol.tofu.dept.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "부서 응답 DTO")
public record DeptResponseDTO(

        @Schema(description = "부서 ID")
        Long id,

        @Schema(description = "부서 이름")
        String name
) {

    @Builder
    public DeptResponseDTO {
    }

    public static DeptResponseDTO toDTO(DeptEntity dept) {
        return DeptResponseDTO.builder()
                .id(dept.getId())
                .name(dept.getName())
                .build();
    }
}
