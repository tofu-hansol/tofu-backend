package com.hansol.tofu.club.domain.dto;

import lombok.Builder;

public record ClubEditRequestDTO(
	String description,
	String accountNumber,
	int fee,
	Long categoryId
) {
	@Builder
	public ClubEditRequestDTO {

	}
}
