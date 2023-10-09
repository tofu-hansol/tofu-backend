package com.hansol.tofu.clubschedule.controller;

import com.hansol.tofu.clubschedule.ClubScheduleService;
import com.hansol.tofu.clubschedule.domain.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "club-schedule", description = "동호회 모임일정 API")
@RestController
@RequestMapping("/api/club-schedule")
@RequiredArgsConstructor
public class ClubScheduleController {

    private final ClubScheduleService clubScheduleService;


    @Operation(summary = "모임일정 추가 API", responses = {
   		@ApiResponse(responseCode = "200", description = "모임일정 추가 성공", content = @Content(schema = @Schema(implementation = Long.class))),
   		@ApiResponse(responseCode = "400", description = "요청값 에러", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
   	})
    @PostMapping
    public BaseHttpResponse<Long> addClubSchedule(@RequestBody @Valid ClubScheduleCreationRequestDTO clubScheduleCreationRequestDTO) {
        return BaseHttpResponse.success(clubScheduleService.addClubSchedule(clubScheduleCreationRequestDTO));
    }

}
