package com.hansol.tofu.category.domain;

import lombok.Builder;

public record CategoryResponseDTO(
	String name
) {
	@Builder
	public CategoryResponseDTO {
	}

	public static CategoryResponseDTO toDTO(CategoryEntity entity) {
		return CategoryResponseDTO.builder()
			.name(entity.getName())
			.build();
	}
}
