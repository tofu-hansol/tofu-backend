package com.hansol.tofu.board.store;

import static com.hansol.tofu.board.domain.entity.QBoardEntity.*;
import static com.hansol.tofu.clubphoto.domain.QClubPhotoEntity.*;
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
import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.board.enums.BoardStatus;
import com.hansol.tofu.clubphoto.domain.ClubPhotoEntity;
import com.hansol.tofu.clubphoto.domain.dto.ClubPhotoResponseDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardQueryStoreImpl implements BoardQueryStore {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<BoardResponseDTO> findFeaturedBoardPages(Pageable pageable) {
		Map<BoardEntity, List<ClubPhotoEntity>> transform = queryFactory
			.select()
			.from(boardEntity)
			.leftJoin(clubPhotoEntity)
				.on(boardEntity.club.id.eq(clubPhotoEntity.board.id)).fetchJoin()
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
						.isMainPhoto(clubPhotoEntity.getIsMainPhoto())
						.createdAt(clubPhotoEntity.getCreatedAt())
						.build())
					.collect(Collectors.toList()))
				.build())
			.collect(Collectors.toList());

		return PageableExecutionUtils.getPage(boardResponseDTOList, pageable, countQuery::fetchOne);

	}

}
