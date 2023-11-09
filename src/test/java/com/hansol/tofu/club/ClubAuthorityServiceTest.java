package com.hansol.tofu.club;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.club.enums.ClubStatus;
import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.clubmember.ClubAuthorityService;
import com.hansol.tofu.clubmember.enums.ClubJoinStatus;
import com.hansol.tofu.clubmember.domain.entity.ClubMemberEntity;
import com.hansol.tofu.clubmember.repository.ClubMemberRepository;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.repository.MemberRepository;
import com.hansol.tofu.mock.WithMockCustomUser;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class ClubAuthorityServiceTest {
	private ClubAuthorityService sut;
	private ClubRepository clubRepository;
	private MemberRepository memberRepository;
	private ClubMemberRepository clubMemberRepository;

	@BeforeEach
	void setUp() {
		clubMemberRepository = mock(ClubMemberRepository.class);
		memberRepository = mock(MemberRepository.class);
		clubRepository = mock(ClubRepository.class);
		sut = new ClubAuthorityService(memberRepository, clubRepository, clubMemberRepository);
	}

	@Test
	@WithMockCustomUser
	void requestJoinClub_동호회가_존재하는_경우_가입신청이_완료된다() {
		var clubId = 1L;
		var clubEntity = ClubEntity.builder().id(clubId).clubStatus(ClubStatus.ACTIVATE).build();
		var memberEntity = MemberEntity.builder().id(1L).memberStatus(MemberStatus.ACTIVATE).build();
		var clubMemberEntity = ClubMemberEntity.builder().club(clubEntity).member(memberEntity).build();
		when(clubRepository.findClubById(clubId)).thenReturn(Optional.of(clubEntity));
		when(memberRepository.findMemberByIdAndMemberStatus(1L, MemberStatus.ACTIVATE)).thenReturn(
			Optional.of(memberEntity));
		when(clubMemberRepository.save(clubMemberEntity)).thenReturn(
			ClubMemberEntity.builder().id(2L).club(clubEntity).member(memberEntity).build()
		);

		Long clubMemberEntityId = sut.requestJoinClub(clubId);

		verify(clubMemberRepository, times(1)).save(clubMemberEntity);
		assertEquals(clubMemberEntityId, 2L);
	}

	@Test
	@WithMockCustomUser
	void cancelJoinClub_동호회가_존재하는_경우_가입신청_취소시_취소에_성공한다() {
		var clubId = 1L;
		var clubMemberEntity = ClubMemberEntity.builder()
			.id(2L)
			.club(ClubEntity.builder().id(clubId).build())
			.member(MemberEntity.builder().id(3L).build())
			.build();
		when(clubMemberRepository.findByClubIdAndMemberId(clubId, 1L)).thenReturn(Optional.of(clubMemberEntity));

		Long clubMemberEntityId = sut.cancelJoinClub(clubId);

		verify(clubMemberRepository, times(1)).deleteById(clubMemberEntity.getId());
		assertEquals(clubMemberEntityId, 2L);
	}

	@Test
	@WithMockCustomUser
	void approveClubJoinRequest_동호회원_가입요청_승인에_성공한다() {
		var clubEntity = ClubEntity.builder().id(4L).build();
		var memberEntity = MemberEntity.builder().id(1L).build();
		var clubMemberEntity = ClubMemberEntity.builder()
			.id(2L)
			.club(clubEntity)
			.member(memberEntity)
			.build();
		when(clubMemberRepository.findByClubIdAndMemberId(clubEntity.getId(), memberEntity.getId()))
			.thenReturn(Optional.of(clubMemberEntity));

		sut.acceptJoinClub(clubEntity.getId(), memberEntity.getId());

		assertEquals(clubMemberEntity.getClubJoinStatus(), ClubJoinStatus.ACCEPTED);
	}

	@Test
	@WithMockCustomUser
	void rejectClubJoinRequest_동호회원_가입요청_거절에_성공한다() {
		var clubEntity = ClubEntity.builder().id(4L).build();
		var memberEntity = MemberEntity.builder().id(1L).build();
		var clubMemberEntity = ClubMemberEntity.builder()
			.id(2L)
			.club(clubEntity)
			.member(memberEntity)
			.build();
		when(clubMemberRepository.findByClubIdAndMemberId(clubEntity.getId(), memberEntity.getId()))
			.thenReturn(Optional.of(clubMemberEntity));


		sut.rejectJoinClub(clubEntity.getId(), memberEntity.getId());


		verify(clubMemberRepository).deleteById(clubMemberEntity.getId());
	}
}
