package com.hansol.tofu.applicant;

import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.clubschedule.ClubScheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubApplicationService {

	private final ApplicantService applicantService;
	private final ClubScheduleService clubScheduleService;


}
