package com.hansol.tofu.clubphoto.repository;

import com.hansol.tofu.clubphoto.domain.ClubPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubPhotoRepository extends JpaRepository<ClubPhotoEntity, Long> {
}
