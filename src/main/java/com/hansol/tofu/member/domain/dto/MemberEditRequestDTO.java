package com.hansol.tofu.member.domain.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "회원정보 수정 요청 DTO")
public record MemberEditRequestDTO(

	@Schema(description = "비밀번호")
	String password,

	@Schema(description = "이름")
	String name,

	@Schema(description = "회사 ID")
	Long companyId,

	@Schema(description = "부서 ID")
	Long deptId,

	@Schema(description = "직급")
	String position,

	@Schema(description = "MBTI")
	String mbti
){
	@Builder
	public MemberEditRequestDTO {
	}
}
