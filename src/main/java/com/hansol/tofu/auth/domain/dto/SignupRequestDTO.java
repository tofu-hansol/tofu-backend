package com.hansol.tofu.auth.domain.dto;

import com.hansol.tofu.dept.domain.DeptEntity;
import com.hansol.tofu.member.domain.MemberEntity;
import jakarta.validation.constraints.Email;
import lombok.Builder;

public record SignupRequestDTO(
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        String email,

        String password,
        String name,
        Long companyId,
        Long deptId,
        String mbti

) {
    @Builder
    public SignupRequestDTO {
    }

    public MemberEntity toEntity(SignupRequestDTO signupRequestDTO, DeptEntity dept) {
        return MemberEntity.builder()
                .email(signupRequestDTO.email())
                .password(signupRequestDTO.password())
                .name(signupRequestDTO.name())
                .dept(dept)
                .mbti(signupRequestDTO.mbti())
                .build();
    }
}
