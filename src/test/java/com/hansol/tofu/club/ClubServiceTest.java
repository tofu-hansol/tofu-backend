package com.hansol.tofu.club;

import com.hansol.tofu.category.domain.CategoryEntity;
import com.hansol.tofu.category.repository.CategoryRepository;
import com.hansol.tofu.club.domain.dto.ClubCreationRequestDTO;
import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.upload.image.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

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

}
