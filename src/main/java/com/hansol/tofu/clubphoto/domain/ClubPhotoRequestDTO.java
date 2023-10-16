package com.hansol.tofu.clubphoto.domain;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

public record ClubPhotoRequestDTO(
        Long id,
        MultipartFile image,
        boolean isMainPhoto
) {
    @Builder
    public ClubPhotoRequestDTO {
    }

    public ClubPhotoEntity toEntity(String imageUrl) {
        return ClubPhotoEntity.builder()
                .id(this.id())
                .isMainPhoto(this.isMainPhoto())
                .imageUrl(imageUrl)
                .build();
    }
}
