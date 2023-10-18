package com.hansol.tofu.club.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.club.enums.ClubStatus;

public interface ClubRepository extends JpaRepository<ClubEntity, Long> {

	Optional<ClubEntity> findClubByIdAndClubStatus(Long clubId, ClubStatus clubStatus);

}
