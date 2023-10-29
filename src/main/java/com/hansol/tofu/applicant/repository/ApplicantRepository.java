package com.hansol.tofu.applicant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hansol.tofu.applicant.domain.ApplicantEntity;
import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;
import com.hansol.tofu.member.domain.MemberEntity;

public interface ApplicantRepository extends JpaRepository<ApplicantEntity, Long> {

	boolean existsByMemberAndClubSchedule(MemberEntity memberEntity, ClubScheduleEntity clubScheduleEntity);

	Optional<ApplicantEntity> findByMemberAndClubSchedule(MemberEntity member, ClubScheduleEntity clubScheduleEntity);

	int countByClubScheduleId(Long clubScheduleId);
}
