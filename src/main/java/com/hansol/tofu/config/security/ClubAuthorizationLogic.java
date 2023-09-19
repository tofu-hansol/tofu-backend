package com.hansol.tofu.config.security;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.club.enums.ClubRole;

import lombok.RequiredArgsConstructor;

@Component("clubAuth")
@RequiredArgsConstructor
public class ClubAuthorizationLogic {

	/**
	 *
	 * @param root		: Spring Security root Context
	 * @param clubId 	: 동호회 ID
	 * @param clubRole	: 동호회 최소 권한
	 * @return			: 동호회 권한이 최소 권한보다 높거나 같으면 true, 아니면 false
	 */
	public boolean decide(MethodSecurityExpressionOperations root, Long clubId, ClubRole clubRole) {
		var userPrincipal = (CustomUserDetails)root.getAuthentication().getPrincipal();
		var clubAuthorizationMap = userPrincipal.getClubAuthorizationDTO();
		int findPriority = clubAuthorizationMap.get(clubId).clubRole().getPriority();

		return findPriority <= clubRole.getPriority();
	}
}
