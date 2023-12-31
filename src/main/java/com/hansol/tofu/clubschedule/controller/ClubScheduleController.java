package com.hansol.tofu.clubschedule.controller;

import com.hansol.tofu.clubmember.annotation.IsPresident;
import com.hansol.tofu.clubschedule.ClubScheduleService;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleEditRequestDTO;
import com.hansol.tofu.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "club-schedule", description = "동호회 모임일정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubScheduleController {

    private final ClubScheduleService clubScheduleService;

    @Operation(summary = "모임일정 추가 API", responses = {
            @ApiResponse(responseCode = "200", description = "모임일정 추가 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "요청값 에러", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 동호회", content = @Content(schema = @Schema(implementation = Long.class))),
    })
    @IsPresident
    @PostMapping("/{clubId}/schedules")
    public BaseHttpResponse<Long> addClubSchedule(@PathVariable Long clubId, @RequestBody @Valid ClubScheduleCreationRequestDTO clubScheduleCreationRequestDTO) {
        return BaseHttpResponse.success(clubScheduleService.addClubSchedule(clubId, clubScheduleCreationRequestDTO));
    }

    @Operation(summary = "모임일정 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "모임일정 변경 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "요청값 에러", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임일정", content = @Content(schema = @Schema(implementation = Long.class))),
    })
    @IsPresident
    @PatchMapping("/{clubId}/schedules/{scheduleId}")
    public BaseHttpResponse<?> editClubSchedule(@PathVariable Long clubId,
                                                @PathVariable Long scheduleId,
                                                @RequestBody @Valid ClubScheduleEditRequestDTO clubScheduleEditRequestDTO
    ) {
        clubScheduleService.editClubSchedule(scheduleId, clubScheduleEditRequestDTO);
        return BaseHttpResponse.successWithNoContent();
    }

    @Operation(summary = "모임일정 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "모임일정 변경 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "요청값 에러", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 모임일정", content = @Content(schema = @Schema(implementation = Long.class))),
    })
    @IsPresident
    @DeleteMapping("/{clubId}/schedules/{scheduleId}")
    public BaseHttpResponse<?> deleteClubSchedule(@PathVariable Long clubId,
                                                @PathVariable Long scheduleId
    ) {
        clubScheduleService.deleteClubSchedule(scheduleId);
        return BaseHttpResponse.successWithNoContent();
    }

}
