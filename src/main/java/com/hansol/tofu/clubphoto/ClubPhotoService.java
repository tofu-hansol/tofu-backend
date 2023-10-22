package com.hansol.tofu.clubphoto;

import static com.hansol.tofu.error.ErrorCode.*;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.clubphoto.domain.dto.ClubPhotoRequestDTO;
import com.hansol.tofu.clubphoto.repository.ClubPhotoRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.upload.image.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubPhotoService {

	private final ClubPhotoRepository clubPhotoRepository;
	private final StorageService storageService;

	public void savePhotos(List<ClubPhotoRequestDTO> clubPhotoRequestDTOs) {
		createPhotos(clubPhotoRequestDTOs);
		editPhotos(clubPhotoRequestDTOs);
		deletePhotos(clubPhotoRequestDTOs);
	}

	private void deletePhotos(List<ClubPhotoRequestDTO> clubPhotoRequestDTOs) {
		clubPhotoRequestDTOs.stream()
			.filter(photo -> photo.id() != null && photo.image() == null)
			.forEach(photo -> {
				var clubPhotoEntity = clubPhotoRepository.findById(photo.id())
					.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB_PHOTO));
				storageService.deleteImage(clubPhotoEntity.getImageUrl());
				clubPhotoRepository.deleteById(photo.id());
			});
	}

	private void createPhotos(List<ClubPhotoRequestDTO> clubPhotoRequestDTOs) {
		clubPhotoRequestDTOs.stream()
			.filter(photo -> photo.id() == null && photo.image() != null)
			.forEach(photo -> {
				String imageUrl = storageService.uploadImage(photo.image(), "images/club/photo");

				var clubPhotoEntity = photo.toEntity(imageUrl);
				// clubPhotoEntity.setBoard(board);
				clubPhotoRepository.save(clubPhotoEntity);
			});
	}

	private void editPhotos(List<ClubPhotoRequestDTO> clubPhotoRequestDTOs) {
		clubPhotoRequestDTOs.stream()
			.filter(photo -> photo.id() != null && photo.image() != null)
			.forEach(photo -> {
				var clubPhotoEntity = clubPhotoRepository.findById(photo.id())
					.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB_PHOTO));
				storageService.deleteImage(clubPhotoEntity.getImageUrl());

				String imageUrl = storageService.uploadImage(photo.image(), "images/club/photo");
				clubPhotoEntity.changePhoto(imageUrl);
			});
	}

}
