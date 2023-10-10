package com.hansol.tofu.clubschedule;

import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.clubschedule.domain.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.clubschedule.repository.ClubScheduleRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubScheduleService {

	private final ClubScheduleRepository clubScheduleRepository;
	private final ClubRepository clubRepository;

	public Long addClubSchedule(Long clubId, ClubScheduleCreationRequestDTO clubScheduleCreationRequestDTO) {
		var clubEntity = clubRepository.findById(clubId)
				.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_CLUB));

		var clubScheduleEntity = clubScheduleCreationRequestDTO
				.toEntity(clubScheduleCreationRequestDTO, clubEntity);

		return clubScheduleRepository.save(clubScheduleEntity).getId();
	}

}
