package com.hansol.tofu.club.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hansol.tofu.club.domain.dto.ClubResponseDTO;

public interface ClubQueryStore {

	Page<ClubResponseDTO> findClubListBy(/* ClubSearchCondition condition */Long categoryId, Pageable pageable);
}
