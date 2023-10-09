package com.hansol.tofu.club.domain.dto;

import com.hansol.tofu.category.domain.CategoryEntity;
import com.hansol.tofu.club.domain.entity.ClubEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "동호회 생성")
public record ClubCreationRequestDTO(
	@NotBlank(message = "동호회 이름은 필수입니다.")
	String name,
	String description,
	String accountNumber,
	int fee,
	Long categoryId
) {
	@Builder
	public ClubCreationRequestDTO {
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
