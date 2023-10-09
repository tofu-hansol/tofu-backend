package com.hansol.tofu.clubschedule;

import com.hansol.tofu.clubschedule.domain.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.clubschedule.repository.ClubScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubScheduleService {

	private final ClubScheduleRepository clubScheduleRepository;

	public Long addClubSchedule(ClubScheduleCreationRequestDTO clubScheduleCreationRequestDTO) {
		var clubScheduleEntity = clubScheduleCreationRequestDTO.toEntity(clubScheduleCreationRequestDTO);

		return clubScheduleRepository.save(clubScheduleEntity).getId();
	}

}
