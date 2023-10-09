package com.hansol.tofu.applicant.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.applicant.ClubApplicationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "applicant", description = "동호회 모임참가 API")
@RestController
@RequestMapping("/api/applicants")
@RequiredArgsConstructor
public class ClubApplicationController {
	private final ClubApplicationService clubApplicationService;
}
