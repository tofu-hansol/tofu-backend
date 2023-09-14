package com.hansol.tofu.company.controller;

import com.hansol.tofu.company.domain.CompanyResponseDTO;
import com.hansol.tofu.company.service.CompanyService;
import com.hansol.tofu.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "company", description = "회사 API")
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회사 조회 성공")
    })
    @Operation(summary = "회사 조회", description = "모든 회사들을 조회합니다.")
    @GetMapping
    public BaseHttpResponse<List<CompanyResponseDTO>> getCompanies() {
        return BaseHttpResponse.success(companyService.getCompanies());
    }

}
