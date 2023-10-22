package com.hansol.tofu.applicant;

import static com.hansol.tofu.error.ErrorCode.*;

import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.applicant.domain.ApplicantEntity;
import com.hansol.tofu.applicant.repository.ApplicantRepository;
import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.global.SecurityUtils;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ApplicantService {

	private final ApplicantRepository applicantRepository;
	private final MemberRepository memberRepository;

	public void addApplicant(ClubScheduleEntity clubScheduleEntity) {

		var memberEntity = memberRepository.findMemberByIdAndMemberStatus(
				SecurityUtils.getCurrentUserId(),
				MemberStatus.ACTIVATE)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		if (isAlreadyApplied(memberEntity, clubScheduleEntity)) {
			throw new BaseException(DUPLICATED_APPLICANT);
		}

		var applicantEntity = ApplicantEntity.builder()
			.member(memberEntity)
			.clubSchedule(clubScheduleEntity)
			.build();

		applicantRepository.save(applicantEntity);
	}

	public void cancelApplicant(ClubScheduleEntity clubScheduleEntity) {
		var memberEntity = memberRepository.findMemberByIdAndMemberStatus(
				SecurityUtils.getCurrentUserId(),
				MemberStatus.ACTIVATE)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var applicantEntity = applicantRepository.findByMemberAndClubSchedule(memberEntity, clubScheduleEntity)
			.orElseThrow(() -> new BaseException(NOT_FOUND_APPLICANT));

		applicantRepository.delete(applicantEntity);
	}

	private boolean isAlreadyApplied(MemberEntity memberEntity, ClubScheduleEntity clubScheduleEntity) {
		return applicantRepository.existsByMemberAndClubSchedule(memberEntity, clubScheduleEntity);
	}
}
