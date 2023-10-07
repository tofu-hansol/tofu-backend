package com.hansol.tofu.club.domain.dto;

import com.hansol.tofu.category.domain.CategoryEntity;
import com.hansol.tofu.club.domain.entity.ClubEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "동호회 생성")
public record ClubRequestDTO(
	String name,
	String description,
	String accountNumber,
	int fee,
	Long categoryId
) {
	@Builder
	public ClubRequestDTO {
	}

	public ClubEntity toEntity(CategoryEntity category) {
		return ClubEntity.builder()
			.name(name)
			.description(description)
			.accountNumber(accountNumber)
			.fee(fee)
			.category(category)
			.build();
	}
}
