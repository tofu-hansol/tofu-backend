package com.hansol.tofu.board.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hansol.tofu.board.domain.dto.BoardResponseDTO;

public interface BoardQueryStore {

	Page<BoardResponseDTO> findFeaturedBoardPages(Pageable pageable);

}
