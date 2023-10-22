package com.hansol.tofu.applicant;

import static com.hansol.tofu.error.ErrorCode.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hansol.tofu.clubschedule.ClubScheduleService;
import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.mock.WithMockCustomUser;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class ClubApplicationServiceTest {

	private ClubApplicationService sut;
	private ClubScheduleService clubScheduleService;
	private ApplicantService applicantService;

	@BeforeEach
	void setUp() {
		clubScheduleService = mock(ClubScheduleService.class);
		applicantService = mock(ApplicantService.class);

		sut = new ClubApplicationService(applicantService, clubScheduleService);
	}

	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void applyClubSchedule_모임일정_참가에_성공한다() throws Exception {
		Long scheduleId = 1L;
		var clubScheduleEntity = ClubScheduleEntity.builder()
			.eventAt(ZonedDateTime.of(LocalDateTime.now().plusDays(1), ZoneId.of("Asia/Seoul")))
			.build();
		when(clubScheduleService.findClubScheduleBy(scheduleId)).thenReturn(clubScheduleEntity);


		sut.applyClubSchedule(3L, scheduleId);


		verify(applicantService, times(1)).addApplicant(clubScheduleEntity);
	}

	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void applyClubSchedule_지난_모임일정에_참가요청시에_실패한다() throws Exception {
		Long scheduleId = 1L;
		var clubScheduleEntity = ClubScheduleEntity.builder()
			.eventAt(ZonedDateTime.of(LocalDateTime.now().minusDays(1), ZoneId.of("Asia/Seoul")))
			.build();
		when(clubScheduleService.findClubScheduleBy(scheduleId)).thenReturn(clubScheduleEntity);


		var exception = Assertions.assertThrows(BaseException.class, () -> sut.applyClubSchedule(3L, scheduleId));


		Assertions.assertEquals(exception.getMessage(), ALREADY_STARTED.getMessage());
	}

	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void cancelClubSchedule_모임일정_참가취소에_성공한다() throws Exception {
		Long scheduleId = 1L;
		var clubScheduleEntity = ClubScheduleEntity.builder()
			.eventAt(ZonedDateTime.of(LocalDateTime.now().minusDays(1), ZoneId.of("Asia/Seoul")))
			.build();
		when(clubScheduleService.findClubScheduleBy(scheduleId)).thenReturn(clubScheduleEntity);


		sut.cancelClubSchedule(3L, scheduleId);


		verify(applicantService, times(1)).cancelApplicant(clubScheduleEntity);
	}

	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void cancelClubSchedule_존재하지_않는_모임일정에_참여취소할때_예외가_발생한다() throws Exception {
		Long scheduleId = 1L;
		when(clubScheduleService.findClubScheduleBy(scheduleId)).thenThrow(new BaseException(NOT_FOUND_CLUB_SCHEDULE));


		var exception = Assertions.assertThrows(BaseException.class, () -> sut.cancelClubSchedule(3L, scheduleId));


		Assertions.assertEquals(exception.getMessage(), NOT_FOUND_CLUB_SCHEDULE.getMessage());
	}


}
