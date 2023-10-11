package com.hansol.tofu.clubschedule;

import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleEditRequestDTO;
import com.hansol.tofu.clubschedule.repository.ClubScheduleRepository;
import com.hansol.tofu.error.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static com.hansol.tofu.error.ErrorCode.NOT_FOUND_CLUB;
import static com.hansol.tofu.error.ErrorCode.NOT_FOUND_CLUB_SCHEDULE;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubScheduleService {

	private final ClubScheduleRepository clubScheduleRepository;
	private final ClubRepository clubRepository;

	public Long addClubSchedule(Long clubId, ClubScheduleCreationRequestDTO clubScheduleCreationRequestDTO) {
		var clubEntity = clubRepository.findById(clubId)
				.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));

		var clubScheduleEntity = clubScheduleCreationRequestDTO
				.toEntity(clubScheduleCreationRequestDTO, clubEntity);

		return clubScheduleRepository.save(clubScheduleEntity).getId();
	}

	public void editClubSchedule(Long scheduleId, ClubScheduleEditRequestDTO clubScheduleEditRequestDTO) {
		var clubScheduleEntity = clubScheduleRepository.findById(scheduleId)
				.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB_SCHEDULE));

		clubScheduleEntity.changeClubSchedule(clubScheduleEditRequestDTO);
	}

}
