package com.hansol.tofu.comment.repository;

import java.util.List;

import com.hansol.tofu.comment.domain.entity.CommentEntity;
import com.hansol.tofu.comment.enums.CommentStatus;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
	List<CommentEntity> findByBoardIdAndCommentStatus(Long boardId, CommentStatus commentStatus);
}
