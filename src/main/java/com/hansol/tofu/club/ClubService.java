package com.hansol.tofu.club;

import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.category.domain.CategoryEntity;
import com.hansol.tofu.category.repository.CategoryRepository;
import com.hansol.tofu.club.domain.dto.ClubRequestDTO;
import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubService {

	private final ClubRepository clubRepository;
	private final CategoryRepository categoryRepository;

	public Long addClub(ClubRequestDTO clubRequestDTO) {
		var categoryEntity = categoryRepository.findById(clubRequestDTO.categoryId())
			.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_CATEGORY));

		var clubEntity = clubRequestDTO.toEntity(categoryEntity);

		return clubRepository.save(clubEntity).getId();
	}
}
