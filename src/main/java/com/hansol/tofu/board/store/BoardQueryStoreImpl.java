package com.hansol.tofu.board.store;

import static com.hansol.tofu.board.domain.entity.QBoardEntity.*;
import static com.hansol.tofu.clubphoto.domain.QClubPhotoEntity.*;
import static com.hansol.tofu.comment.domain.entity.QCommentEntity.*;
import static com.hansol.tofu.dept.domain.QDeptEntity.*;
import static com.hansol.tofu.member.domain.QMemberEntity.*;
import static com.querydsl.core.group.GroupBy.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.hansol.tofu.board.domain.dto.BoardResponseDTO;
import com.hansol.tofu.board.domain.dto.QBoardResponseDTO;
import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.board.enums.BoardStatus;
import com.hansol.tofu.clubphoto.domain.ClubPhotoEntity;
import com.hansol.tofu.clubphoto.domain.dto.ClubPhotoResponseDTO;
import com.hansol.tofu.clubphoto.domain.dto.QClubPhotoResponseDTO;
import com.hansol.tofu.comment.domain.entity.QCommentEntity;
import com.hansol.tofu.comment.enums.CommentStatus;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardQueryStoreImpl implements BoardQueryStore {

	private final JPAQueryFactory queryFactory;

	// TOD
	@Override
	public Page<BoardResponseDTO> findFeaturedBoardPages(Pageable pageable) {
		Map<BoardEntity, List<ClubPhotoEntity>> transform = queryFactory
			.select(boardEntity, commentEntity.count(), list(clubPhotoEntity))
			.from(boardEntity)
			.leftJoin(clubPhotoEntity)
			.on(boardEntity.clubId.eq(clubPhotoEntity.board.id)).fetchJoin()
			.leftJoin(memberEntity)
			.on(boardEntity.member.id.eq(memberEntity.id)).fetchJoin()
			.where(boardEntity.boardStatus.eq(BoardStatus.FEATURED))
			.orderBy(boardEntity.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.transform(groupBy(boardEntity).as(list(clubPhotoEntity)));

		JPAQuery<Long> countQuery = queryFactory.select(boardEntity.count())
			.from(boardEntity)
			.where(boardEntity.boardStatus.eq(BoardStatus.FEATURED));

		var boardResponseDTOList = transform.entrySet().stream()
			.map(entry -> BoardResponseDTO.builder()
				.boardId(entry.getKey().getId())
				.memberId(entry.getKey().getMember().getId())
				.clubId(entry.getKey().getClubId())
				.memberProfileUrl(entry.getKey().getMember().getProfileUrl())
				.deptName(entry.getKey().getMember().getDept().getName())
				.memberName(entry.getKey().getMember().getName())
				.title(entry.getKey().getTitle())
				.content(entry.getKey().getContent())
				.createdAt(entry.getKey().getCreatedAt())
				.updatedAt(entry.getKey().getUpdatedAt())
				.photoList(entry.getValue().stream()
					.map(clubPhotoEntity -> ClubPhotoResponseDTO.builder()
						.id(clubPhotoEntity.getId())
						.imageUrl(clubPhotoEntity.getImageUrl())
						.createdAt(clubPhotoEntity.getCreatedAt())
						.build())
					.collect(Collectors.toList()))
				.build())
			.collect(Collectors.toList());

		return PageableExecutionUtils.getPage(boardResponseDTOList, pageable, countQuery::fetchOne);

	}

	@Override
	public Page<BoardResponseDTO> findClubBoardPages(Long clubId, Pageable pageable) {
		QCommentEntity commentEntity = new QCommentEntity("commentEntity");

		// TODO: https://okky.kr/questions/1079343
		// TODO: https://jojoldu.tistory.com/529
		List<Long> ids = queryFactory.select(boardEntity.id)
			.from(boardEntity)
			.where(boardEntity.clubId.eq(clubId).and(boardEntity.boardStatus.ne(BoardStatus.DELETED)))
			.orderBy(boardEntity.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		if (ids.isEmpty()) {
			return Page.empty();
		}

		Map<Long, BoardResponseDTO> transform = queryFactory.from(boardEntity)
			.leftJoin(clubPhotoEntity)
				.on(boardEntity.id.eq(clubPhotoEntity.board.id)).fetchJoin()
			.innerJoin(boardEntity.member, memberEntity)
			.innerJoin(memberEntity.dept, deptEntity)
			.where(boardEntity.id.in(ids))
			.orderBy(boardEntity.createdAt.desc())
			.transform(groupBy(boardEntity.id)
				.as(new QBoardResponseDTO(
					boardEntity.id,
					memberEntity.id,
					boardEntity.clubId,
					memberEntity.profileUrl,
					deptEntity.name,
					memberEntity.name,
					boardEntity.title,
					boardEntity.content,
					ExpressionUtils.as(JPAExpressions.select(commentEntity.count())
						.from(commentEntity)
						.where(commentEntity.commentStatus.eq(CommentStatus.PUBLISHED)
							.and(commentEntity.board.id.eq(boardEntity.id))), "commentCount"),
					boardEntity.createdAt,
					boardEntity.updatedAt,
					list(new QClubPhotoResponseDTO(clubPhotoEntity.id, clubPhotoEntity.imageUrl, clubPhotoEntity.createdAt))
				))
			);

		JPAQuery<Long> countQuery = queryFactory.select(boardEntity.id.count())
			.from(boardEntity)
			.where(boardEntity.clubId.eq(clubId).and(boardEntity.boardStatus.ne(BoardStatus.DELETED)));

		List<BoardResponseDTO> boardResponseDTOList = transform.values().stream().collect(Collectors.toList());

		return PageableExecutionUtils.getPage(boardResponseDTOList, pageable, countQuery::fetchOne);
	}



}
