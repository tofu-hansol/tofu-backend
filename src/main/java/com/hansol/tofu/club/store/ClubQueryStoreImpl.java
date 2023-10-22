package com.hansol.tofu.club.store;

import static com.hansol.tofu.club.domain.entity.QClubEntity.*;
import static com.hansol.tofu.clubmember.domain.entity.QClubMemberEntity.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.hansol.tofu.club.domain.dto.ClubResponseDTO;
import com.hansol.tofu.club.domain.dto.QClubResponseDTO;
import com.hansol.tofu.clubmember.enums.ClubJoinStatus;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ClubQueryStoreImpl implements ClubQueryStore {

	private final JPAQueryFactory queryFactory;

	/* TODO: https://nomoreft.tistory.com/45 */
	@Override
	public Page<ClubResponseDTO> findClubListBy(/* ClubSearchCondition condition */Long categoryId, Pageable pageable) {
		var clubEntities = queryFactory.select(new QClubResponseDTO(
				clubEntity.id,
				clubEntity.profileUrl,
				clubEntity.name,
				ExpressionUtils.as(JPAExpressions.select(clubMemberEntity.count())
					.from(clubMemberEntity)
					.where(clubMemberEntity.clubJoinStatus.eq(ClubJoinStatus.ACCEPTED)
						.and(clubMemberEntity.club.id.eq(clubEntity.id))), "memberCount"),
				clubEntity.createdAt
			))
			.from(clubEntity)
			.where(clubEntity.category.id.eq(categoryId))
			.orderBy(clubEntity.name.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(clubEntity.count())
			.from(clubEntity)
			.where(clubEntity.category.id.eq(categoryId));

		return PageableExecutionUtils.getPage(clubEntities, pageable, countQuery::fetchOne);
	}
}
