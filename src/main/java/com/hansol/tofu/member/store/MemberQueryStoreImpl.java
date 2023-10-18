package com.hansol.tofu.member.store;

import static com.hansol.tofu.member.domain.QMemberEntity.*;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hansol.tofu.member.domain.QMemberEntity;
import com.hansol.tofu.member.domain.dto.MemberMyProfileResponseDTO;
import com.hansol.tofu.member.domain.dto.QMemberMyProfileResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberQueryStoreImpl implements MemberQueryStore {

	private final JPAQueryFactory queryFactory;

	// TODO: ClubAuthority에 집어넣는다.
	// clubAuthority에서 clubAuthorityStore 통해서 회원 ID 에 대해 권한정보 가져오고
	// memberService 통해서 회원정보를 가져온다.
	// 즉 지금 MemberQueryStoreImpl은 필요없음
	@Override
	public List<MemberMyProfileResponseDTO> findMyProfile(Long memberId) {
		// return queryFactory
			// .select(new QMemberMyProfileResponseDTO())
			// .from(memberEntity)
			// .where(memberEntity.id.eq(memberId))
		//
		//
		// 	))
		// 	.from(QMemberEntity.memberEntity)
		return Collections.emptyList();
		// return queryFactory
		// 	.select(new QMemberMyProfileResponseDTO(
		// 		memberEntity.email,
		// 		memberEntity.name,
		// 		memberEntity.profileImageUrl,
		// 		memberEntity.companyName,
		// 		memberEntity.deptName,
		// 		memberEntity.positionName,
		// 		memberEntity.mbti,
		// 		clubMemberEntity.club.id,
		// 		clubMemberEntity.club.name,
		// 		clubMemberEntity.club.profileImageUrl,
		// 		clubMemberEntity.clubMemberType
		// 	))
		// 	.from(clubMemberEntity)
		// 	.leftJoin(clubMemberEntity.club, clubEntity).fetchJoin()
		// 	.leftJoin(clubMemberEntity.member, memberEntity).fetchJoin()
		// 	.where(clubMemberEntity.member.id.eq(memberId))
		// 	.fetch();
	}
		// return queryFactory
		// 	.selectFrom(clubMemberEntity)
		// 	.leftJoin(clubMemberEntity.club, clubEntity).fetchJoin()
		// 	// https://velog.io/@pppp0722/JPQL-fetch-join%EC%97%90%EC%84%9C-on%EC%A0%88%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%A0-%EC%88%98-%EC%9E%88%EC%9D%84%EA%B9%8C
		// 	// TODO: ERROR : with-clause not allowed on fetched associations; use filters
		// 	.leftJoin(clubMemberEntity.member, memberEntity).fetchJoin()
		// 	.distinct()
		// 	.where(clubMemberEntity.member.id.eq(memberId))
		// 	.fetch();
}
