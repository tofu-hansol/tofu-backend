package com.hansol.tofu.dept.controller;

import com.hansol.tofu.dept.domain.DeptResponseDTO;
import com.hansol.tofu.dept.service.DeptService;
import com.hansol.tofu.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "dept", description = "부서 API")
@RestController
@RequestMapping("/api/depts")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "부서 조회 성공")
    })
    @Operation(summary = "부서 조회", description = "회사 ID를 이용하여 부서를 조회")
    @GetMapping
    public BaseHttpResponse<List<DeptResponseDTO>> getDepts(@RequestParam Long companyId) {
        return BaseHttpResponse.success(deptService.getDepts(companyId));
    }

}
