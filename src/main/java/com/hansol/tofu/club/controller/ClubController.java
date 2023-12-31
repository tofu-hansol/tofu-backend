package com.hansol.tofu.club.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hansol.tofu.club.ClubService;
import com.hansol.tofu.club.domain.dto.ClubCreationRequestDTO;
import com.hansol.tofu.club.domain.dto.ClubDetailResponseDTO;
import com.hansol.tofu.club.domain.dto.ClubEditRequestDTO;
import com.hansol.tofu.club.domain.dto.ClubResponseDTO;
import com.hansol.tofu.clubmember.annotation.IsPresident;
import com.hansol.tofu.global.BaseHttpResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "club", description = "동호회 API")
@RestController
@RequestMapping("/api/clubs")
public class ClubController {

	private final ClubService clubService;

	public ClubController(ClubService clubService) {
		this.clubService = clubService;
	}

	@Operation(summary = "동호회 생성 요청 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 생성 요청 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@PostMapping
	public BaseHttpResponse<Long> addClub(@RequestBody @Valid ClubCreationRequestDTO clubRequestDTO) {
		return BaseHttpResponse.success(clubService.addClub(clubRequestDTO));
	}

	@Operation(summary = "동호회 수정 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 상세 수정 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리, 존재하지 않는 동호회 정보", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@IsPresident
	@PatchMapping(value = "/{clubId}", consumes = "multipart/form-data")
	public BaseHttpResponse<Long> editClub(
		@PathVariable Long clubId,
		@ModelAttribute @Valid ClubEditRequestDTO clubEditRequestDTO,
		@RequestPart(required = false) MultipartFile backgroundImage,
		@RequestPart(required = false) MultipartFile profileImage
	) {
		return BaseHttpResponse.success(clubService.editClub(clubId, clubEditRequestDTO, backgroundImage, profileImage));
	}

	@Operation(summary = "동호회 목록 조회 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 목록 조회 성공", content = @Content(schema = @Schema(implementation = Long.class))),
	})
	@GetMapping
	public BaseHttpResponse<Page<ClubResponseDTO>> getClubList(@RequestParam Long categoryId, Pageable pageable) {
		return BaseHttpResponse.success(clubService.getClubListBy(categoryId, pageable));
	}

	@Operation(summary = "동호회 상세 조회 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 상세 조회 성공", content = @Content(schema = @Schema(implementation = Long.class))),
	})
	@GetMapping("/{clubId}")
	public BaseHttpResponse<ClubDetailResponseDTO> getClubDetail(@PathVariable Long clubId) {
		return BaseHttpResponse.success(clubService.getClubDetail(clubId));
	}


}
