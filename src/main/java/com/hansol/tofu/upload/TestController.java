package com.hansol.tofu.upload;

import com.hansol.tofu.club.domain.dto.ClubAuth;
import com.hansol.tofu.member.MemberService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.global.BaseHttpResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

// TODO: 테스트 목적으로써 추후 제거
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "test", description = "테스트 API")
@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

	private final MemberService memberService;

	@GetMapping
	@Operation(summary = "테스트 문자열 출력 API")
	public BaseHttpResponse<String> printTest() {
		return BaseHttpResponse.success("테스트 API");
	}


	@GetMapping("/clubAuth")
	@Operation(summary = "동호회관리자 권한 테스트 API")
	public BaseHttpResponse<List<ClubAuth>> getClubAuthsByMember() {
		return BaseHttpResponse.success(memberService.getCurrentMemberClubAuthority());
	}



}
