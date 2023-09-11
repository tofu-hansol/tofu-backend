package com.hansol.tofu.dept.domain;

import lombok.Builder;

public record DeptResponseDTO(
        Long id,
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
