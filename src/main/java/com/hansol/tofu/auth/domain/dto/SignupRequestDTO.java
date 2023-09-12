package com.hansol.tofu.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Schema(description = "회원가입 요청 DTO")
public record SignupRequestDTO(

        @Schema(description = "이메일")
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        String email,

        @Schema(description = "비밀번호")
        String password,

        @Schema(description = "이름")
        String name,

        @Schema(description = "부서 ID")
        Long deptId,

        @Schema(description = "MBTI")
        String mbti

) {
    @Builder
    public SignupRequestDTO {
    }
}
