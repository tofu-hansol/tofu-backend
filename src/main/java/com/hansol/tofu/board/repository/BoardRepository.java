package com.hansol.tofu.board.repository;

import com.hansol.tofu.board.domain.dto.BoardResponseDTO;
import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.board.store.BoardQueryStore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, BoardQueryStore {

	Page<BoardResponseDTO> findFeaturedBoardPages(Pageable pageable);
	Page<BoardResponseDTO> findClubBoardPages(Long clubId, Pageable pageable);
}
