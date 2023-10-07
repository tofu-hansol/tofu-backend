package com.hansol.tofu.club.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.club.ClubService;
import com.hansol.tofu.club.domain.dto.ClubRequestDTO;
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
		// @ApiResponse(responseCode = "400", description = "요청값 에러", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
		// @ApiResponse(responseCode = "409", description = "존재하는 회원", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@PostMapping
	public BaseHttpResponse<Long> addClub(@RequestBody @Valid ClubRequestDTO clubRequestDTO) {
		return BaseHttpResponse.success(clubService.addClub(clubRequestDTO));
	}
}
