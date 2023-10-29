package com.hansol.tofu.applicant;

import static com.hansol.tofu.error.ErrorCode.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.applicant.domain.dto.ClubApplicationResponseDTO;
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

	@Transactional(readOnly = true)
	public List<ClubApplicationResponseDTO> getClubSchedules(Long clubId) {
		var clubScheduleEntityList = clubScheduleService.findClubSchedulesWithin(clubId, 3);

		var clubApplicationResponseDTOList = clubScheduleEntityList.stream()
			.map(clubSchedule ->
				ClubApplicationResponseDTO.of(clubSchedule, applicantService.countApplicants(clubSchedule.getId()))
		).collect(Collectors.toList());

		return clubApplicationResponseDTOList;

	}

	public void applyClubSchedule(Long clubId, Long scheduleId) {
		var clubScheduleEntity = clubScheduleService.findClubSchedule(scheduleId);

		if (isAlreadyStarted(clubScheduleEntity)) {
			throw new BaseException(ALREADY_STARTED);
		}

		applicantService.addApplicant(clubScheduleEntity);
	}

	public void cancelClubSchedule(Long clubId, Long scheduleId) {
		var clubScheduleEntity = clubScheduleService.findClubSchedule(scheduleId);

		applicantService.cancelApplicant(clubScheduleEntity);
	}

	private boolean isAlreadyStarted(ClubScheduleEntity clubSchedule) {
		return LocalDateTime.now().isAfter(clubSchedule.getEventAt().toLocalDateTime());
	}
}
