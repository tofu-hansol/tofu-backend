package com.hansol.tofu.clubmember;

import static com.hansol.tofu.error.ErrorCode.*;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.clubmember.domain.dto.ClubJoinResponseDTO;
import com.hansol.tofu.clubmember.domain.dto.ClubMemberResponseDTO;
import com.hansol.tofu.clubmember.domain.entity.ClubMemberEntity;
import com.hansol.tofu.clubmember.repository.ClubMemberRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.global.SecurityUtils;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class ClubAuthorityService {
	private final MemberRepository memberRepository;
	private final ClubRepository clubRepository;
	private final ClubMemberRepository clubMemberRepository;

	@Transactional(readOnly = true)
	public List<ClubJoinResponseDTO> getJoinedClubList(Long memberId) {
		return clubMemberRepository.findClubJoinListBy(memberId);
	}

	// TODO : 요청시 중복된 요청이 있는지 확인 필요
	public Long requestJoinClub(Long clubId) {
		Long memberId = SecurityUtils.getCurrentUserId();
		var memberEntity = memberRepository.findMemberByIdAndMemberStatus(memberId, MemberStatus.ACTIVATE)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var clubEntity = clubRepository.findClubById(clubId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB));

		var clubMemberEntity = ClubMemberEntity.builder()
			.member(memberEntity)
			.club(clubEntity)
			.build();

		return clubMemberRepository.save(clubMemberEntity).getId();
	}

	@Transactional(readOnly = true)
	public List<ClubMemberResponseDTO> getClubMembers(Long clubId) {
		return clubMemberRepository.findClubMembers(clubId);
	}

	public Long cancelJoinClub(Long clubId) {
		Long memberId = SecurityUtils.getCurrentUserId();

		var clubMemberEntity = clubMemberRepository.findByClubIdAndMemberId(clubId, memberId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB_MEMBER));
		clubMemberRepository.deleteById(clubMemberEntity.getId());

		return clubMemberEntity.getId();
	}

	public Long acceptJoinClub(Long clubId, Long memberId) {
		var clubMemberEntity = clubMemberRepository.findByClubIdAndMemberId(clubId, memberId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB_MEMBER));
		clubMemberEntity.accept();

		return clubMemberEntity.getId();
	}

	public Long rejectJoinClub(Long clubId, Long memberId) {
		var clubMemberEntity = clubMemberRepository.findByClubIdAndMemberId(clubId, memberId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CLUB_MEMBER));
		clubMemberEntity.reject();

		return clubMemberEntity.getId();
	}

}
