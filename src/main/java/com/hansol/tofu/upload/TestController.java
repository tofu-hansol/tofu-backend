package com.hansol.tofu.upload;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.global.BaseHttpResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "member", description = "테스트 API")
@RestController
@RequestMapping("/api/tests")
public class TestController {

	@GetMapping
	@Operation(summary = "테스트 문자열 출력 API")
	public BaseHttpResponse<String> printTest() {
		return BaseHttpResponse.success("테스트 API");
	}
}
