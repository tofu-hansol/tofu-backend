package com.hansol.tofu.clubmember.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.clubmember.ClubAuthorityService;
import com.hansol.tofu.clubmember.annotation.IsPresident;
import com.hansol.tofu.global.BaseHttpResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "club_member", description = "동호회 권한 API")
@RestController
@RequestMapping("/api/clubs")
public class ClubAuthorityController {

	private final ClubAuthorityService clubAuthorityService;

	public ClubAuthorityController(ClubAuthorityService clubAuthorityService) {
		this.clubAuthorityService = clubAuthorityService;
	}


	@Operation(summary = "동호회 가입 신청 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 가입 신청 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 동호회/회원 정보", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@PostMapping("/{clubId}/members")
	public BaseHttpResponse<Long> requestJoinClub(@PathVariable Long clubId) {
		return BaseHttpResponse.success(clubAuthorityService.requestJoinClub(clubId));
	}

	@Operation(summary = "동호회 가입 신청 취소 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 가입 신청 취소 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 동호회/회원 정보", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@DeleteMapping("/{clubId}/members")
	public BaseHttpResponse<Long> cancelJoinClub(@PathVariable Long clubId) {
		return BaseHttpResponse.success(clubAuthorityService.cancelJoinClub(clubId));
	}

	@Operation(summary = "동호회 가입 승인 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 가입 승인 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 동호회/회원 정보", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@IsPresident
	@PatchMapping("/{clubId}/members/{memberId}")
	public BaseHttpResponse<Long> acceptJoinClub(@PathVariable Long clubId, @PathVariable Long memberId) {
		return BaseHttpResponse.success(clubAuthorityService.acceptJoinClub(clubId, memberId));
	}

	@Operation(summary = "동호회 가입 거절 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 가입 거절 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 동호회/회원 정보", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@IsPresident
	@DeleteMapping("/{clubId}/members/{memberId}")
	public BaseHttpResponse<Long> rejectJoinClub(@PathVariable Long clubId, @PathVariable Long memberId) {
		return BaseHttpResponse.success(clubAuthorityService.rejectJoinClub(clubId, memberId));
	}
}