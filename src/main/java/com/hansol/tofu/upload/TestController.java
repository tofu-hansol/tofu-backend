package com.hansol.tofu.upload;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.tofu.club.annotation.IsManager;
import com.hansol.tofu.club.annotation.IsMember;
import com.hansol.tofu.club.annotation.IsPresident;
import com.hansol.tofu.club.domain.dto.ClubAuthorization;
import com.hansol.tofu.club.domain.dto.ClubAuthorizationDTO;
import com.hansol.tofu.global.BaseHttpResponse;
import com.hansol.tofu.member.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

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
    public BaseHttpResponse<Map<Long, ClubAuthorizationDTO>> getClubAuthsByMember() {
        return BaseHttpResponse.success(memberService.getCurrentMemberClubAuthority());
    }

    @GetMapping("/clubs/{clubId}/club-president")
    @IsPresident
    @Operation(summary = "동호회장만 사용 가능한 API")
    public BaseHttpResponse<String> testClubPresident(@PathVariable Long clubId) {
        return BaseHttpResponse.success(testService.president(clubId));
    }

    @GetMapping("/clubs/{clubId}/club-manager")
    @IsManager
    @Operation(summary = "동호회장, 총무만 사용 가능한 API")
    public BaseHttpResponse<String> testClubManager(@PathVariable Long clubId) {
		return BaseHttpResponse.success(testService.manager(clubId));
	}


    @GetMapping("/clubs/{clubId}/club-member")
    @IsMember
    @Operation(summary = "동호회장, 총무, 회원만 사용 가능한 API")
    public BaseHttpResponse<String> testClubMember(@PathVariable Long clubId) {
        return BaseHttpResponse.success(testService.member(clubId));
    }


}
