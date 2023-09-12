package com.hansol.tofu.member.domain;

import com.hansol.tofu.dept.domain.DeptEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Schema(description = "회원가입 요청 DTO")
public record MemberRequestDTO(

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
    public MemberRequestDTO {
    }

    public MemberEntity toEntity(MemberRequestDTO memberRequestDTO, DeptEntity dept) {
        return MemberEntity.builder()
                .email(memberRequestDTO.email())
                .password(memberRequestDTO.password())
                .name(memberRequestDTO.name())
                .dept(dept)
                .mbti(memberRequestDTO.mbti())
                .build();
    }
}