package com.hansol.tofu.clubmember.controller;

import com.hansol.tofu.clubmember.ClubAuthorityService;
import com.hansol.tofu.clubmember.annotation.IsPresident;
import com.hansol.tofu.clubmember.domain.dto.ClubJoinResponseDTO;
import com.hansol.tofu.clubmember.domain.dto.ClubMemberResponseDTO;
import com.hansol.tofu.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "club_member", description = "동호회 권한 API")
@RestController
@RequestMapping("/api/club-authority")
public class ClubAuthorityController {

	private final ClubAuthorityService clubAuthorityService;

	public ClubAuthorityController(ClubAuthorityService clubAuthorityService) {
		this.clubAuthorityService = clubAuthorityService;
	}

	@Operation(summary = "특정 동호회에 가입한 회원 목록 조회 API")
	@GetMapping("/{clubId}/members")
	public BaseHttpResponse<List<ClubMemberResponseDTO>> getClubMembers(@PathVariable Long clubId) {
		return BaseHttpResponse.success(clubAuthorityService.getClubMembers(clubId));
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

	@Operation(summary = "특정 유저가 소속된 동호회 조회 API")
	@GetMapping("/{memberId}")
	public BaseHttpResponse<List<ClubJoinResponseDTO>> getJoinedClubList(@PathVariable Long memberId) {
		return BaseHttpResponse.success(clubAuthorityService.getJoinedClubList(memberId));
	}
}
