package com.hansol.tofu.company.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "회사 목록 DTO")
public record CompanyResponseDTO(

        @Schema(description = "회사 ID")
        Long id,
        @Schema(description = "회사 이름")
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
