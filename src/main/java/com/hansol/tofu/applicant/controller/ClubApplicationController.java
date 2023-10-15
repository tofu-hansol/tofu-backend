package com.hansol.tofu.applicant.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.applicant.ClubApplicationService;
import com.hansol.tofu.club.annotation.IsMember;
import com.hansol.tofu.global.BaseHttpResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "applicant", description = "동호회 모임참가 API")
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubApplicationController {
	private final ClubApplicationService clubApplicationService;

	@Operation(summary = "동호회 모임일정 참가신청 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 배경사진 변경 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "400", description = "모임일 이후 일정에 신청요청 시 실패", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 동호회 모임일정 신청자 정보", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@IsMember
	@PostMapping("/{clubId}/schedules/{scheduleId}/applicants")
	public void applyClubSchedule(@PathVariable Long clubId, @PathVariable Long scheduleId) {
		clubApplicationService.applyClubSchedule(clubId, scheduleId);
	}

	@Operation(summary = "동호회 모임일정 참가취소 API", responses = {
		@ApiResponse(responseCode = "200", description = "동호회 배경사진 변경 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 동호회 모임일정 신청자 정보", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@IsMember
	@DeleteMapping("/{clubId}/schedules/{scheduleId}/applicants")
	public void cancelClubSchedule(@PathVariable Long clubId, @PathVariable Long scheduleId) {
		clubApplicationService.cancelClubSchedule(clubId, scheduleId);
	}
}
