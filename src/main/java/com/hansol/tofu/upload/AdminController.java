package com.hansol.tofu.upload;

import com.hansol.tofu.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "admin", description = "Admin API")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@GetMapping
	@Operation(summary = "Admin Test 문자열 출력 API")
	public BaseHttpResponse<String> printTest() {
		return BaseHttpResponse.success("테스트 API");
	}

	@GetMapping("/all")
	@Operation(summary = "Admin Test 문자열 출력 API")
	public BaseHttpResponse<String> printAll() {
		return BaseHttpResponse.success("printAll");
	}
}
