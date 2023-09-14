package com.hansol.tofu.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "로그인 요청 DTO")
public record LoginRequestDTO(
        @Schema(description = "유저 이메일")
        @NotBlank(message = "계정명을 입력해주세요.")
        String email,

        @Schema(description = "유저 비밀번호")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
    @Builder
    public LoginRequestDTO {
    }
}
