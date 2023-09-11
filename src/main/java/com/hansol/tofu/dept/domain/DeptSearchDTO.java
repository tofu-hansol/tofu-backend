package com.hansol.tofu.dept.domain;

import lombok.Builder;

public record DeptSearchDTO(
        Long companyId
) {

    @Builder
    public DeptSearchDTO {
    }
}
