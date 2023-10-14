package com.hansol.tofu.clubschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;

public interface ClubScheduleRepository extends JpaRepository<ClubScheduleEntity, Long> {
}
