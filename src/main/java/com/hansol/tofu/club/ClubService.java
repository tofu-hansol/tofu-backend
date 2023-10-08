package com.hansol.tofu.club;

import static com.hansol.tofu.error.ErrorCode.*;

import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hansol.tofu.category.repository.CategoryRepository;
import com.hansol.tofu.club.domain.dto.ClubCreationRequestDTO;
import com.hansol.tofu.club.domain.dto.ClubEditRequestDTO;
import com.hansol.tofu.club.domain.entity.ClubMemberEntity;
import com.hansol.tofu.club.repository.ClubMemberRepository;
import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.global.SecurityUtils;
import com.hansol.tofu.member.repository.MemberRepository;
import com.hansol.tofu.upload.image.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubService {

	private final ClubRepository clubRepository;
	private final CategoryRepository categoryRepository;
	private final StorageService storageService;
	private final ClubMemberRepository clubMemberRepository;
	private final MemberRepository memberRepository;

	public Long addClub(ClubCreationRequestDTO clubRequestDTO) {
		var categoryEntity = categoryRepository.findById(clubRequestDTO.categoryId())
			.orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY));

		var clubEntity = clubRequestDTO.toEntity(categoryEntity);

		return clubRepository.save(clubEntity).getId();
	}

	public Long editClub(Long clubId, ClubEditRequestDTO clubEditRequestDTO) {
		var categoryEntity = categoryRepository.findById(clubEditRequestDTO.categoryId())
			.orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY));
		var clubEntity = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));

		clubEntity.changeClubInfo(clubEditRequestDTO, categoryEntity);
		return clubId;
	}

	public Long changeBackgroundImage(Long clubId, MultipartFile backgroundImage) {
		var clubEntity = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));

		String imageUrl = storageService.uploadImage(backgroundImage, "images/club/");
		clubEntity.changeBackgroundImage(imageUrl);

		return clubId;
	}

	public Long changeProfileImage(Long clubId, MultipartFile profileImage) {
		var clubEntity = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));

		String imageUrl = storageService.uploadImage(profileImage, "images/club/");
		clubEntity.changeProfileImage(imageUrl);

		return clubId;
	}

	public Long requestJoinClub(Long clubId) {
		Long memberId = SecurityUtils.getCurrentUserId();
		var memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var clubEntity = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));

		var clubMemberEntity = ClubMemberEntity.builder()
			.member(memberEntity)
			.club(clubEntity)
			.build();

		return clubMemberRepository.save(clubMemberEntity).getId();
	}

	public Long cancelJoinClub(Long clubId) {
		Long memberId = SecurityUtils.getCurrentUserId();

		var clubMemberEntity = clubMemberRepository.findByClubIdAndMemberId(clubId, memberId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB_MEMBER));
		clubMemberRepository.deleteById(clubMemberEntity.getId());

		return clubId;
	}


}
