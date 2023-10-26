package com.hansol.tofu.clubmember.store;

import static com.hansol.tofu.club.domain.entity.QClubEntity.*;
import static com.hansol.tofu.clubmember.domain.entity.QClubMemberEntity.*;
import static com.hansol.tofu.member.domain.QMemberEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hansol.tofu.clubmember.domain.dto.ClubJoinResponseDTO;
import com.hansol.tofu.clubmember.domain.dto.ClubMemberResponseDTO;
import com.hansol.tofu.clubmember.domain.dto.QClubJoinResponseDTO;
import com.hansol.tofu.clubmember.domain.dto.QClubMemberResponseDTO;
import com.hansol.tofu.clubmember.domain.entity.ClubMemberEntity;
import com.hansol.tofu.clubmember.enums.ClubJoinStatus;
import com.hansol.tofu.dept.domain.QDeptEntity;
import com.hansol.tofu.member.domain.QMemberEntity;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ClubMemberQueryStoreImpl implements ClubMemberQueryStore {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ClubMemberEntity> findAllByMemberId(Long memberId) {
		return queryFactory.selectFrom(clubMemberEntity)
			.leftJoin(clubMemberEntity.club, clubEntity)
			.fetchJoin()
			// https://velog.io/@pppp0722/JPQL-fetch-join%EC%97%90%EC%84%9C-on%EC%A0%88%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%A0-%EC%88%98-%EC%9E%88%EC%9D%84%EA%B9%8C
			// TODO: ERROR : with-clause not allowed on fetched associations; use filters
			.leftJoin(clubMemberEntity.member, memberEntity)
			.fetchJoin()
			.distinct()
			.where(clubMemberEntity.member.id.eq(memberId)
				.and(clubMemberEntity.clubJoinStatus.eq(ClubJoinStatus.ACCEPTED)))
			.fetch();
	}

	@Override
	public List<ClubJoinResponseDTO> findClubJoinListBy(Long memberId) {
		return queryFactory.select(new QClubJoinResponseDTO(clubEntity.name, clubMemberEntity.clubRole.stringValue(),
				clubMemberEntity.createdAt.stringValue()))
			.from(clubMemberEntity)
			.leftJoin(clubEntity)
			.fetchJoin()
			.where(clubMemberEntity.member.id.eq(memberId))
			.orderBy(clubMemberEntity.createdAt.desc())
			.fetch();
	}

	@Override
	public List<ClubMemberResponseDTO> findClubMembers(Long clubId) {
		QDeptEntity deptEntity = new QDeptEntity("SubDeptEntity");
		return queryFactory.select(new QClubMemberResponseDTO(
				clubMemberEntity.id,
				memberEntity.id,
				memberEntity.profileUrl,
				ExpressionUtils.as(JPAExpressions.select(deptEntity.name)
					.from(deptEntity)
					.where(memberEntity.dept.id.eq(deptEntity.id)), "deptName"),
				memberEntity.name,
				memberEntity.userRole.stringValue(),
				clubMemberEntity.createdAt.stringValue()))
			.from(clubMemberEntity)
			.leftJoin(memberEntity)
				.on(clubMemberEntity.member.id.eq(memberEntity.id)).fetchJoin()
			.where(clubMemberEntity.club.id.eq(clubId).and(clubMemberEntity.clubJoinStatus.eq(ClubJoinStatus.ACCEPTED)))
			.orderBy(clubMemberEntity.createdAt.desc())
			.fetch().stream().distinct().toList();
	}
}
