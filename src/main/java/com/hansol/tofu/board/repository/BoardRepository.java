package com.hansol.tofu.board.repository;

import java.util.List;

import com.hansol.tofu.board.domain.dto.BoardResponseDTO;
import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.board.enums.BoardStatus;
import com.hansol.tofu.board.store.BoardQueryStore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, BoardQueryStore {

	Page<BoardResponseDTO> findFeaturedBoardPages(Pageable pageable);
	Page<BoardResponseDTO> findClubBoardPages(Long clubId, Pageable pageable);

	@Query("select b.id from BoardEntity b where b.clubId = :clubId and b.boardStatus <> :boardStatus")
	List<Long> findBoardIdListByClubId(@Param("clubId") Long clubId, @Param("boardStatus") BoardStatus boardStatus);
}
