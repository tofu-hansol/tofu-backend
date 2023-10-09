package com.hansol.tofu.clubschedule.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.clubschedule.ClubScheduleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "club-schedule", description = "동호회 모임일정 API")
@RestController
@RequestMapping("/api/club-schedule")
@RequiredArgsConstructor
public class ClubScheduleController {

	private final ClubScheduleService clubScheduleService;

}
