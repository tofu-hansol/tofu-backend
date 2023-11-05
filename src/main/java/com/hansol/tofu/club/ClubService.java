package com.hansol.tofu.club;

import static com.hansol.tofu.error.ErrorCode.*;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hansol.tofu.category.repository.CategoryRepository;
import com.hansol.tofu.club.domain.dto.ClubCreationRequestDTO;
import com.hansol.tofu.club.domain.dto.ClubDetailResponseDTO;
import com.hansol.tofu.club.domain.dto.ClubEditRequestDTO;
import com.hansol.tofu.club.domain.dto.ClubResponseDTO;
import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.upload.image.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubService {

	private final ClubRepository clubRepository;
	private final CategoryRepository categoryRepository;
	private final StorageService storageService;


	@Transactional(readOnly = true)
	public Page<ClubResponseDTO> getClubListBy(Long categoryId, Pageable pageable) {
		long count = clubRepository.count();

		if (count == 0) {
			return Page.empty();
		}
		return clubRepository.findClubListBy(categoryId, pageable);
	}

	@Transactional(readOnly = true)
	public ClubDetailResponseDTO getClubDetail(Long clubId) {
		return clubRepository.findClubDetail(clubId).orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));
	}

	public Long addClub(ClubCreationRequestDTO clubRequestDTO) {
		var categoryEntity = categoryRepository.findById(clubRequestDTO.categoryId())
			.orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY));

		var clubEntity = clubRequestDTO.toEntity(categoryEntity);

		return clubRepository.save(clubEntity).getId();
	}

	public Long editClub(Long clubId,
		ClubEditRequestDTO clubEditRequestDTO,
		MultipartFile backgroundImage,
		MultipartFile profileImage
	) {
		var categoryEntity = categoryRepository.findById(clubEditRequestDTO.categoryId())
			.orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY));
		var clubEntity = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));

		clubEntity.changeClubInfo(clubEditRequestDTO, categoryEntity);

		if (backgroundImage != null) {
			changeBackgroundImage(clubId, backgroundImage);
		}

		if (profileImage != null) {
			changeProfileImage(clubId, profileImage);
		}

		return clubId;
	}

	private Long changeBackgroundImage(Long clubId, MultipartFile backgroundImage) {
		var clubEntity = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));

		String imageUrl = storageService.uploadImage(backgroundImage, "images/club/");
		clubEntity.changeBackgroundImage(imageUrl);

		return clubId;
	}

	private Long changeProfileImage(Long clubId, MultipartFile profileImage) {
		var clubEntity = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));

		String imageUrl = storageService.uploadImage(profileImage, "images/club/");
		clubEntity.changeProfileImage(imageUrl);

		return clubId;
	}
}
