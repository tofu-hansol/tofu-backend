package com.hansol.tofu.auth.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "이메일 전송 요청 DTO")
public record EmailRequestDTO(
        @Schema(description = "받는 사람 이메일")
        @Email(message = "이메일 형식이 아닙니다.")
        String to,

        @Schema(description = "제목")
        @NotBlank
        String subject,

        @Schema(description = "내용")
        String content
) {
    @Builder
    public EmailRequestDTO {
    }
}
