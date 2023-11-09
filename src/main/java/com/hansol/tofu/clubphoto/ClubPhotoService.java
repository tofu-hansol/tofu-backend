package com.hansol.tofu.clubphoto;

import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.clubphoto.domain.ClubPhotoEntity;
import com.hansol.tofu.clubphoto.domain.dto.ClubPhotoRequestDTO;
import com.hansol.tofu.clubphoto.repository.ClubPhotoRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.upload.image.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static com.hansol.tofu.error.ErrorCode.NOT_FOUND_CLUB_PHOTO;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubPhotoService {

	private final ClubPhotoRepository clubPhotoRepository;
	private final StorageService storageService;

	@Transactional(readOnly = true)
	public List<ClubPhotoEntity> findClubPhotoByBoardIdIn(List<Long> boardIds) {
		return clubPhotoRepository.findByBoardIdIn(boardIds);
	}

	public void deletePhotos(List<ClubPhotoRequestDTO> clubPhotoRequestDTOs) {
		clubPhotoRequestDTOs.stream()
			.filter(photo -> photo.id() != null && photo.image() == null)
			.forEach(photo -> {
				var clubPhotoEntity = clubPhotoRepository.findById(photo.id())
					.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB_PHOTO));
				storageService.deleteImage(clubPhotoEntity.getImageUrl());
				clubPhotoRepository.deleteById(photo.id());
			});
	}

	public void createPhotos(BoardEntity boardEntity, List<MultipartFile> clubPhotos) {
		clubPhotos.forEach(photo -> {
				String imageUrl = storageService.uploadImage(photo, "images/club/photo");

				var clubPhotoEntity = ClubPhotoEntity.builder().board(boardEntity).imageUrl(imageUrl).build();
				clubPhotoRepository.save(clubPhotoEntity);
			});
	}

	public void editPhotos(List<ClubPhotoRequestDTO> clubPhotoRequestDTOs) {
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
