package com.hansol.tofu.dept.controller;

import com.hansol.tofu.dept.domain.DeptResponseDTO;
import com.hansol.tofu.dept.service.DeptService;
import com.hansol.tofu.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@SecurityRequirement(name = "Bearer Authentication")
//@Tag(name = "member", description = "회원 API")
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @GetMapping
    @Operation(summary = "부서 조회 API")
    public BaseHttpResponse<List<DeptResponseDTO>> getDepts() {
        return BaseHttpResponse.success(deptService.getDepts());
    }

}
