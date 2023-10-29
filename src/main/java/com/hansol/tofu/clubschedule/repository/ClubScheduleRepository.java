package com.hansol.tofu.clubschedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;

public interface ClubScheduleRepository extends JpaRepository<ClubScheduleEntity, Long> {
	List<ClubScheduleEntity> findAllByClubId(Long clubId);
}
