package com.hansol.tofu.clubschedule;

import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.clubschedule.repository.ClubScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubScheduleService {

	private final ClubScheduleRepository clubScheduleRepository;

}
