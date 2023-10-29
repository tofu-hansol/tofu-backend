package com.hansol.tofu.comment;

import static com.hansol.tofu.error.ErrorCode.*;
import static com.hansol.tofu.member.enums.MemberStatus.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.board.repository.BoardRepository;
import com.hansol.tofu.comment.domain.dto.CommentCreationRequestDTO;
import com.hansol.tofu.comment.domain.dto.CommentEditRequestDTO;
import com.hansol.tofu.comment.domain.dto.CommentResponseDTO;
import com.hansol.tofu.comment.enums.CommentStatus;
import com.hansol.tofu.comment.repository.CommentRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.global.SecurityUtils;
import com.hansol.tofu.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class CommentService {

	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public List<CommentResponseDTO> getComments(Long boardId) {
		var commentEntities = commentRepository.findByBoardIdAndCommentStatus(boardId, CommentStatus.PUBLISHED);

		return commentEntities.stream().map(commentEntity -> {
				var memberEntity = commentEntity.getMember();
				return CommentResponseDTO.of(commentEntity, memberEntity);
			}
		).collect(Collectors.toList());
	}

	public Long addComment(Long boardId, CommentCreationRequestDTO commentCreationRequestDTO) {
		Long memberId = SecurityUtils.getCurrentUserId();
		var memberEntity = memberRepository.findMemberByIdAndMemberStatus(memberId, ACTIVATE)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_FOUND_BOARD));
		var commentEntity = commentCreationRequestDTO.toEntity(memberEntity, boardEntity);

		return commentRepository.save(commentEntity).getId();
	}

	public void editComment(Long boardId, Long commentId, CommentEditRequestDTO commentEditRequestDTO) {
		Long memberId = SecurityUtils.getCurrentUserId();
		var commentEntity = commentRepository.findById(commentId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_COMMENT));

		if (!commentEntity.getMember().getId().equals(memberId)) {
			throw new BaseException(ACCESS_DENIED);
		}

		commentEntity.changeComment(commentEditRequestDTO.content());
	}

	public void deleteComment(Long boardId, Long commentId) {
		Long memberId = SecurityUtils.getCurrentUserId();
		var commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new BaseException(NOT_FOUND_BOARD));

		if (!commentEntity.getMember().getId().equals(memberId)) {
			throw new BaseException(ACCESS_DENIED);
		}

		commentEntity.deleteComment();
	}

}
