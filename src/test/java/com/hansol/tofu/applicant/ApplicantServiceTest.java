package com.hansol.tofu.applicant;

import static com.hansol.tofu.member.enums.MemberStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hansol.tofu.applicant.domain.ApplicantEntity;
import com.hansol.tofu.applicant.repository.ApplicantRepository;
import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.error.ErrorCode;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.repository.MemberRepository;
import com.hansol.tofu.mock.WithMockCustomUser;

@ExtendWith(SpringExtension.class)
@ContextConfiguration	// load spring security context
class ApplicantServiceTest {

	private ApplicantService sut;
	private MemberRepository memberRepository;
	private ApplicantRepository applicantRepository;

	@BeforeEach
	void setUp() {
		applicantRepository = mock(ApplicantRepository.class);
		memberRepository = mock(MemberRepository.class);

		sut = new ApplicantService(applicantRepository, memberRepository);
	}

	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void addApplicant_모임일정_참가에_성공한다() throws Exception {
		var clubScheduleEntity = ClubScheduleEntity.builder().build();
		var memberEntity = MemberEntity.builder().build();
		var applicantEntity = ApplicantEntity.builder()
			.clubSchedule(clubScheduleEntity)
			.member(memberEntity)
			.build();
		when(applicantRepository.existsByMemberAndClubSchedule(memberEntity, clubScheduleEntity)).thenReturn(false);
		when(memberRepository.findMemberByIdAndMemberStatus(1L, ACTIVATE)).thenReturn(Optional.of(memberEntity));
		when(applicantRepository.save(applicantEntity)).thenReturn(applicantEntity);


		sut.addApplicant(clubScheduleEntity);


		verify(applicantRepository, times(1)).save(applicantEntity);
	}


	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void addApplicant_이미참가신청한_유저가_중복신청할_경우_예외가_발생한다() {
		var clubScheduleEntity = ClubScheduleEntity.builder().build();
		var memberEntity = MemberEntity.builder().build();
		when(memberRepository.findMemberByIdAndMemberStatus(1L, ACTIVATE)).thenReturn(Optional.of(memberEntity));
		when(applicantRepository.existsByMemberAndClubSchedule(memberEntity, clubScheduleEntity)).thenReturn(true);


		var exception = assertThrows(
			BaseException.class,
			() -> sut.addApplicant(clubScheduleEntity)
		);

		assertEquals(ErrorCode.DUPLICATED_APPLICANT.getMessage(), exception.getMessage());
	}

	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void cancelApplicant_모임일정_참여취소에_성공한다() throws Exception {
		var clubScheduleEntity = ClubScheduleEntity.builder().build();
		var memberEntity = MemberEntity.builder().build();
		var applicantEntity = ApplicantEntity.builder()
			.member(memberEntity)
			.clubSchedule(clubScheduleEntity)
			.build();
		when(memberRepository.findMemberByIdAndMemberStatus(1L, ACTIVATE)).thenReturn(Optional.of(memberEntity));
		when(applicantRepository.findByMemberAndClubSchedule(memberEntity, clubScheduleEntity)).thenReturn(Optional.of(applicantEntity));


		sut.cancelApplicant(clubScheduleEntity);


		verify(applicantRepository, times(1)).delete(applicantEntity);
	}

	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void cancelApplicant_존재하지_않는_모임일정_참여정보에_취소요청할_때에_예외가_발생한다() throws Exception {
		var clubScheduleEntity = ClubScheduleEntity.builder().build();
		var memberEntity = MemberEntity.builder().build();
		when(memberRepository.findMemberByIdAndMemberStatus(1L, ACTIVATE)).thenReturn(Optional.of(memberEntity));
		when(applicantRepository.findByMemberAndClubSchedule(memberEntity, clubScheduleEntity)).thenReturn(Optional.empty());


		var exception = assertThrows(
			BaseException.class,
			() -> sut.cancelApplicant(clubScheduleEntity)
		);

		assertEquals(ErrorCode.NOT_FOUND_APPLICANT.getMessage(), exception.getMessage());
	}
}
