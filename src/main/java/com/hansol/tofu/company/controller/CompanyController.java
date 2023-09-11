package com.hansol.tofu.company.controller;

import com.hansol.tofu.company.domain.CompanyResponseDTO;
import com.hansol.tofu.company.service.CompanyService;
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
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    @Operation(summary = "회사 조회 API")
    public BaseHttpResponse<List<CompanyResponseDTO>> getCompanies() {
        return BaseHttpResponse.success(companyService.getCompanies());
    }

}
