package com.hansol.tofu.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema(description = "회원가입 요청 DTO")
public record SignupRequestDTO(


        @Email(message = "이메일 형식이 올바르지 않습니다.")
//        @Email(regexp = "^[a-zA-Z0-9._%+-]+@hansol\\.com$", message = "한솔그룹 이메일만 가능합니다")
        @Schema(description = "이메일")
        String email,

        @Schema(description = "비밀번호")
        String password,

        @Schema(description = "이름")
        String name,

        @Schema(description = "부서 ID")
        @Positive(message = "부서 ID는 1 이상의 숫자만 가능합니다.")
        Long deptId,

        @Schema(description = "MBTI")
        @Size(min = 4, max = 4, message = "MBTI는 4자리로 입력해주세요.")
        String mbti

) {
    @Builder
    public SignupRequestDTO {
    }
}
