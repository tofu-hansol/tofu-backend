package com.hansol.tofu.club.store;

import static com.hansol.tofu.board.domain.entity.QBoardEntity.*;
import static com.hansol.tofu.club.domain.entity.QClubEntity.*;
import static com.hansol.tofu.clubmember.domain.entity.QClubMemberEntity.*;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.hansol.tofu.board.enums.BoardStatus;
import com.hansol.tofu.club.domain.dto.ClubDetailResponseDTO;
import com.hansol.tofu.club.domain.dto.ClubResponseDTO;
import com.hansol.tofu.club.domain.dto.QClubDetailResponseDTO;
import com.hansol.tofu.club.domain.dto.QClubResponseDTO;
import com.hansol.tofu.club.enums.ClubStatus;
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
				clubEntity.createdAt,
				clubEntity.description
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

	@Override
	public Optional<ClubDetailResponseDTO> findClubDetail(Long clubId) {
		return Optional.ofNullable(queryFactory
			.select(new QClubDetailResponseDTO(
				clubEntity.category.id,
				clubEntity.id,
				clubEntity.name,
				clubEntity.profileUrl,
				clubEntity.backgroundUrl,
				clubEntity.description,
				clubEntity.fee,
				clubEntity.accountNumber,
				ExpressionUtils.as(JPAExpressions.select(clubMemberEntity.count())
					.from(clubMemberEntity)
					.where(clubMemberEntity.clubJoinStatus.eq(ClubJoinStatus.ACCEPTED)
						.and(clubMemberEntity.club.id.eq(clubEntity.id))), "memberCount"),
				ExpressionUtils.as(JPAExpressions.select(boardEntity.count())
					.from(boardEntity)
					.where(boardEntity.boardStatus.ne(BoardStatus.DELETED)
						.and(boardEntity.clubId.eq(clubEntity.id))), "boardCount")
			))
			.from(clubEntity)
			.where(clubEntity.id.eq(clubId).and(clubEntity.clubStatus.eq(ClubStatus.ACTIVATE)))
			.fetchOne());
	}
}
