package com.hansol.tofu.applicant;

import static com.hansol.tofu.error.ErrorCode.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.clubschedule.ClubScheduleService;
import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;
import com.hansol.tofu.error.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubApplicationService {

	private final ApplicantService applicantService;
	private final ClubScheduleService clubScheduleService;

	public void applyClubSchedule(Long clubId, Long scheduleId) {

		var clubScheduleEntity = clubScheduleService.findClubScheduleBy(scheduleId);

		if (isAlreadyStarted(clubScheduleEntity)) {
			throw new BaseException(ALREADY_STARTED);
		}

		applicantService.addApplicant(clubScheduleEntity);
	}

	public void cancelClubSchedule(Long clubId, Long scheduleId) {
		var clubScheduleEntity = clubScheduleService.findClubScheduleBy(scheduleId);

		applicantService.cancelApplicant(clubScheduleEntity);
	}

	private boolean isAlreadyStarted(ClubScheduleEntity clubSchedule) {
		return LocalDateTime.now().isAfter(clubSchedule.getEventAt().toLocalDateTime());
	}
}
