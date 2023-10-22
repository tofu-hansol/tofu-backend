package com.hansol.tofu.club;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import com.hansol.tofu.category.domain.CategoryEntity;
import com.hansol.tofu.category.repository.CategoryRepository;
import com.hansol.tofu.club.domain.dto.ClubCreationRequestDTO;
import com.hansol.tofu.club.domain.dto.ClubEditRequestDTO;
import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.clubmember.ClubAuthorityService;
import com.hansol.tofu.upload.image.StorageService;

class ClubServiceTest {

	private ClubService sut;
	private ClubRepository clubRepository;
	private CategoryRepository categoryRepository;
	private StorageService storageService;

	@BeforeEach
	void setUp() {
		categoryRepository = mock(CategoryRepository.class);
		clubRepository = mock(ClubRepository.class);
		storageService = mock(StorageService.class);
		sut = new ClubService(clubRepository, categoryRepository, storageService);
	}

	@Test
	void addClub_동호회_생성에_성공한다() throws Exception {
		var categoryEntity = CategoryEntity.builder().name("운동").build();
		var clubRequestDTO = ClubCreationRequestDTO.builder()
			.name("tofu-club")
			.description("tofu club")
			.accountNumber("123-456-789")
			.fee(10000)
			.categoryId(1L)
			.build();
		var clubEntity = clubRequestDTO.toEntity(categoryEntity);
		when(categoryRepository.findById(clubRequestDTO.categoryId())).thenReturn(Optional.of(categoryEntity));
		when(clubRepository.save(clubEntity)).thenReturn(clubEntity);


		sut.addClub(clubRequestDTO);


		verify(clubRepository).save(Mockito.any(ClubEntity.class));
		verify(categoryRepository).findById(clubRequestDTO.categoryId());
	}

	@Test
	void editClub_동호회_정보변경_요청시_성공한다() throws Exception {
		Long clubId = 1L;
		var categoryEntity = CategoryEntity.builder().name("운동").build();
		var clubEditRequestDTO = ClubEditRequestDTO.builder()
			.description("동호회설명변경")
			.accountNumber("222-2222-2222")
			.categoryId(2L)
			.fee(200000)
			.build();
		var clubEntity = ClubEntity.builder()
			.id(clubId)
			.name("tofu-club")
			.description("tofu club")
			.accountNumber("123-456-789")
			.fee(10000)
			.category(CategoryEntity.builder().name("요리").build())
			.build();

		when(categoryRepository.findById(clubEditRequestDTO.categoryId())).thenReturn(Optional.of(categoryEntity));
		when(clubRepository.findById(clubId)).thenReturn(Optional.of(clubEntity));


		sut.editClub(clubId, clubEditRequestDTO);


		assertEquals(clubEntity.getDescription(), clubEditRequestDTO.description());
		assertEquals(clubEntity.getAccountNumber(), clubEditRequestDTO.accountNumber());
		assertEquals(clubEntity.getCategory().getName(), categoryEntity.getName());
		assertEquals(clubEntity.getFee(), clubEditRequestDTO.fee());
	}

	@Test
	void changeBackgroundImage_동호회_배경사진_변경에_성공한다() {
		MockMultipartFile backgroundImage = new MockMultipartFile("backgroundImage", "back.jpg", "image/jpeg", "test".getBytes());
		var clubEntity = ClubEntity.builder().id(2L).build();

		when(clubRepository.findById(2L)).thenReturn(Optional.of(clubEntity));
		when(storageService.uploadImage(backgroundImage, "images/club/")).thenReturn("http://image.com/backgroundImage");


		sut.changeBackgroundImage(clubEntity.getId(), backgroundImage);


		assertEquals(clubEntity.getBackgroundUrl(), "http://image.com/backgroundImage");
		verify(storageService).uploadImage(backgroundImage, "images/club/");
	}

	@Test
	void changeProfileImage_동호회_프로필사진_변경에_성공한다() {
		MockMultipartFile profileImage = new MockMultipartFile("profileImage", "club_profile.jpg", "image/jpeg", "test".getBytes());
		var clubEntity = ClubEntity.builder().id(2L).build();

		when(clubRepository.findById(2L)).thenReturn(Optional.of(clubEntity));
		when(storageService.uploadImage(profileImage, "images/club/")).thenReturn("http://image.com/profileImage");


		sut.changeProfileImage(clubEntity.getId(), profileImage);


		assertEquals(clubEntity.getProfileUrl(), "http://image.com/profileImage");
		verify(storageService).uploadImage(profileImage, "images/club/");
	}
}
