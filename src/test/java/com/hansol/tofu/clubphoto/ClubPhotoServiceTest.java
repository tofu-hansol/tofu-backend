package com.hansol.tofu.clubphoto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.hansol.tofu.clubphoto.domain.ClubPhotoEntity;
import com.hansol.tofu.clubphoto.domain.dto.ClubPhotoRequestDTO;
import com.hansol.tofu.clubphoto.repository.ClubPhotoRepository;
import com.hansol.tofu.upload.image.StorageService;

class ClubPhotoServiceTest {

	private ClubPhotoService sut;
	private ClubPhotoRepository clubPhotoRepository;
	private StorageService storageService;

	@BeforeEach
	void setUp() {
		clubPhotoRepository = mock(ClubPhotoRepository.class);
		storageService = mock(StorageService.class);
		sut = new ClubPhotoService(clubPhotoRepository, storageService);
	}

	@Test
	void savePhotos_동호회_사진_저장에_성공한다() {
		var board1Image = new MockMultipartFile(
			"board1",
			"board1.jpg",
			"image/jpeg",
			"board1".getBytes()
		);
		var clubPhotoRequestDTOs = List.of(
			ClubPhotoRequestDTO.builder()
				.isMainPhoto(true)
				.image(board1Image)
				.build()
		);
		when(storageService.uploadImage(board1Image, "images/club/photo")).thenReturn("https://board1Image.com");

		sut.savePhotos(clubPhotoRequestDTOs);

		verify(clubPhotoRepository, times(1)).save(
			ClubPhotoEntity.builder()
				.isMainPhoto(true)
				.imageUrl("https://board1Image.com")
				.build()
		);
	}

	@Test
	void savePhotos_동호회_사진_변경에_성공한다() {
		var board2Image = new MockMultipartFile(
			"board2",
			"board2.jpg",
			"image/jpeg",
			"board2".getBytes()
		);
		var clubPhotoEntity = ClubPhotoEntity.builder()
			.id(2L)
			.imageUrl("https://beforeBoardImage.com")
			.build();
		var clubPhotoRequestDTOs = List.of(
			ClubPhotoRequestDTO.builder()
				.id(2L)
				.isMainPhoto(false)
				.image(board2Image)
				.build()
		);
		when(clubPhotoRepository.findById(2L)).thenReturn(Optional.of(clubPhotoEntity));
		when(storageService.uploadImage(board2Image, "images/club/photo")).thenReturn("https://afterBoardImage.com");

		sut.savePhotos(clubPhotoRequestDTOs);

		assertEquals("https://afterBoardImage.com", clubPhotoEntity.getImageUrl());
	}

	@Test
	void savePhotos_동호회_사진_삭제에_성공한다() {
		var clubPhotoRequestDTOs = List.of(
			ClubPhotoRequestDTO.builder()
				.id(3L)
				.build()
		);
		var clubPhotoEntity = ClubPhotoEntity.builder()
			.id(3L)
			.imageUrl("https://beforeBoardImage.com")
			.build();
		when(clubPhotoRepository.findById(3L)).thenReturn(Optional.of(clubPhotoEntity));

		sut.savePhotos(clubPhotoRequestDTOs);

		verify(clubPhotoRepository, times(1)).deleteById(3L);
	}
}
