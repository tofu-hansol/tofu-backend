package com.hansol.tofu.clubphoto.repository;

import java.util.List;

import com.hansol.tofu.clubphoto.domain.ClubPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubPhotoRepository extends JpaRepository<ClubPhotoEntity, Long> {

	List<ClubPhotoEntity> findByBoardIdIn(List<Long> boardIds);

}
