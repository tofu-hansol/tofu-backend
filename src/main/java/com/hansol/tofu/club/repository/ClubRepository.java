package com.hansol.tofu.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hansol.tofu.club.domain.entity.ClubEntity;

public interface ClubRepository extends JpaRepository<ClubEntity, Long> {

}
