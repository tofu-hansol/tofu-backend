package com.hansol.tofu.upload;

import com.hansol.tofu.club.annotation.IsManager;
import com.hansol.tofu.club.annotation.IsMember;
import com.hansol.tofu.club.annotation.IsPresident;
import com.hansol.tofu.club.domain.dto.ClubAuthorization;
import com.hansol.tofu.global.BaseHttpResponse;
import com.hansol.tofu.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO: 테스트 목적으로써 추후 제거
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "test", description = "테스트 API")
@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

    private final MemberService memberService;
    private final TestService testService;

    @GetMapping
    @Operation(summary = "테스트 문자열 출력 API")
    public BaseHttpResponse<String> printTest() {
        return BaseHttpResponse.success("테스트 API");
    }


    @GetMapping("/clubAuth")
    @Operation(summary = "현재 본인의 동호회 권한 가져오는 API")
    public BaseHttpResponse<List<ClubAuthorization>> getClubAuthsByMember() {
        return BaseHttpResponse.success(memberService.getCurrentMemberClubAuthority());
    }

    @GetMapping("/club-president")
    @IsPresident
    @Operation(summary = "동호회장만 사용 가능한 API")
    public BaseHttpResponse<String> testClubPresident() {
        return BaseHttpResponse.success(testService.president());
    }

    @GetMapping("/club-manager")
    @IsManager
    @Operation(summary = "동호회장, 총무만 사용 가능한 API")
    public BaseHttpResponse<String> testClubManager() {
        return BaseHttpResponse.success(testService.manager());
    }

    @GetMapping("/club-member")
    // 스프링 시큐리티의 root 객체를 참조, 이를 매개변수로 제공
    @IsMember
    @Operation(summary = "동호회장, 총무, 회원만 사용 가능한 API")
    public BaseHttpResponse<String> testClubMember() {
        return BaseHttpResponse.success(testService.member());
    }


}
