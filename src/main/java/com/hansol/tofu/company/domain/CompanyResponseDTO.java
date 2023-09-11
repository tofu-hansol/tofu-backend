package com.hansol.tofu.company.domain;

import lombok.Builder;

public record CompanyResponseDTO(
        Long id,
        String name
) {

    @Builder
    public CompanyResponseDTO {
    }

    public static CompanyResponseDTO toDTO(CompanyEntity company) {
        return CompanyResponseDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .build();
    }
}
