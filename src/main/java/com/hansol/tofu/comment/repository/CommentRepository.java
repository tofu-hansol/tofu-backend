package com.hansol.tofu.comment.repository;

import com.hansol.tofu.comment.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
