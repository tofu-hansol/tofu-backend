package com.hansol.tofu.board.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.board.BoardService;
import com.hansol.tofu.board.domain.dto.BoardCreationRequestDTO;
import com.hansol.tofu.board.domain.dto.BoardEditRequestDTO;
import com.hansol.tofu.board.domain.dto.BoardResponseDTO;
import com.hansol.tofu.clubmember.annotation.IsMember;
import com.hansol.tofu.clubphoto.domain.dto.ClubPhotoResponseDTO;
import com.hansol.tofu.global.BaseHttpResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "board", description = "동호회 게시글 API")
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@Operation(summary = "동호회 게시글 작성 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 게시글 작성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "400", description = "입력정보 누락(제목, 내용)", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@IsMember
	@PostMapping("/{clubId}/boards")
	public BaseHttpResponse<Long> addBoard(@PathVariable Long clubId,
		@RequestBody @Valid BoardCreationRequestDTO boardCreationRequestDTO) {
		return BaseHttpResponse.success(boardService.addBoard(clubId, boardCreationRequestDTO));
	}

	@Operation(summary = "동호회 게시글 수정 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 게시글 수정 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "400", description = "입력정보 누락(제목, 내용)", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "403", description = "본인이 아닌 다른 회원 게시글 수정 시도", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@IsMember
	@PatchMapping("/{clubId}/boards/{boardId}")
	public BaseHttpResponse<?> editBoard(
		@PathVariable Long clubId,
		@PathVariable Long boardId,
		@RequestBody @Valid BoardEditRequestDTO boardEditRequestDTO) {
		boardService.editBoard(clubId, boardId, boardEditRequestDTO);
		return BaseHttpResponse.successWithNoContent();
	}

	@Operation(summary = "동호회 게시글 삭제 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 게시글 수정 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "403", description = "본인이 아닌 다른 회원 게시글 삭제 시도", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@IsMember
	@DeleteMapping("/{clubId}/boards/{boardId}")
	public BaseHttpResponse<?> deleteBoard(@PathVariable Long clubId, @PathVariable Long boardId) {
		boardService.deleteBoard(clubId, boardId);
		return BaseHttpResponse.successWithNoContent();
	}

	@Operation(summary = "홍보 게시글 조회 API")
	@GetMapping("/boards/featured")
	public BaseHttpResponse<Page<BoardResponseDTO>> getFeaturedBoards(Pageable pageable) {
		return BaseHttpResponse.success(boardService.getFeaturedBoardPages(pageable));
	}

	@Operation(summary = "동호회 게시글 조회 API")
	@GetMapping("/{clubId}/boards")
	public BaseHttpResponse<Page<BoardResponseDTO>> getClubBoards(@PathVariable Long clubId, Pageable pageable) {
		return BaseHttpResponse.success(boardService.getClubBoardPages(clubId, pageable));
	}

	@Operation(summary = "특정 동호회 게시글 내 모든 사진 목록 조회 API")
	@GetMapping("/{clubId}/boards/photos")
	public BaseHttpResponse<List<ClubPhotoResponseDTO>> getClubBoardPhotos(@PathVariable Long clubId) {
		return BaseHttpResponse.success(boardService.getClubPhotos(clubId));
	}



}
