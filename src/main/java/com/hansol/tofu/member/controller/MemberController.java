package com.hansol.tofu.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hansol.tofu.global.BaseHttpResponse;
import com.hansol.tofu.member.MemberService;
import com.hansol.tofu.member.domain.dto.MemberEditRequestDTO;
import com.hansol.tofu.member.domain.dto.MemberMyProfileResponseDTO;
import com.hansol.tofu.member.domain.dto.MemberProfileResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "member", description = "회원 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@Operation(summary = "회원정보 변경 API", responses = {
		@ApiResponse(responseCode = "200", description = "회원정보 변경 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "401", description = "존재하지 않는 회원", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 부서", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@PatchMapping
	public BaseHttpResponse<Long> editMemberProfile(@RequestBody @Valid MemberEditRequestDTO memberEditRequestDTO) {
		return BaseHttpResponse.success(memberService.editMemberProfile(memberEditRequestDTO));
	}

	@Operation(summary = "회원프로필 이미지 변경 API", responses = {
		@ApiResponse(responseCode = "200", description = "회원프로필 이미지 변경 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "401", description = "존재하지 않는 회원", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
		@ApiResponse(responseCode = "500", description = "업로드 실패", content = @Content(schema = @Schema(implementation = Long.class))),
	})
	@PatchMapping("/image")
	public BaseHttpResponse<Long> changeMemberProfileImage(@RequestPart MultipartFile image) {
		return BaseHttpResponse.success(memberService.changeMemberProfileImage(image));
	}

	@Operation(summary = "내 상세정보 조회 API", responses = {
		@ApiResponse(responseCode = "200", description = "내 상세정보 조회 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "401", description = "존재하지 않는 회원", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@GetMapping("/me")
	public BaseHttpResponse<MemberMyProfileResponseDTO> getMyProfile() {
		return BaseHttpResponse.success(memberService.getMyProfile());
	}

	@Operation(summary = "상대방 정보 조회 API", responses = {
		@ApiResponse(responseCode = "200", description = "상대방 정보 조회 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "401", description = "존재하지 않는 회원", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
	})
	@GetMapping("/{memberId}")
	public BaseHttpResponse<MemberProfileResponseDTO> getMemberProfile(@PathVariable Long memberId) {
		return BaseHttpResponse.success(memberService.getMemberProfile(memberId));
	}

}
