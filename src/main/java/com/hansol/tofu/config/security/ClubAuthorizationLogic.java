package com.hansol.tofu.config.security;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.club.enums.ClubRole;

import lombok.RequiredArgsConstructor;

@Component("clubAuth")
@RequiredArgsConstructor
public class ClubAuthorizationLogic {

	public boolean decide(MethodSecurityExpressionOperations root, Long clubId, ClubRole clubRole) {
		var userPrincipal = (CustomUserDetails)root.getAuthentication().getPrincipal();
		var clubAuthorizationMap = userPrincipal.getClubAuthorizationDTO();
		int findPriority = clubAuthorizationMap.get(clubId).clubRole().getPriority();

		if (findPriority > clubRole.getPriority()) {
			return false;
		}
		return true;
	}
}
