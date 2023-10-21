package com.hansol.tofu.club.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.club.store.ClubQueryStore;

public interface ClubRepository extends JpaRepository<ClubEntity, Long>, ClubQueryStore {

	@Query("select c from ClubEntity c where c.id = :clubId "
		+ "and c.clubStatus = com.hansol.tofu.club.enums.ClubStatus.ACTIVATE")
	Optional<ClubEntity> findClubById(Long clubId);
}
