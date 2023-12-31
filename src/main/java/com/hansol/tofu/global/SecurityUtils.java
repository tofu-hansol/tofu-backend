package com.hansol.tofu.global;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.clubmember.domain.dto.ClubAuthorizationDTO;

public class SecurityUtils {

	public static String getCurrentPrinciple() {
		return (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static Collection<? extends GrantedAuthority> getCurrentAuthorities() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	}

	public static Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
		return principal.getMemberId();
	}

	public static String getCurrentUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
		return principal.getUsername();
	}

	public static Map<Long, ClubAuthorizationDTO> getClubAuthorization() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
		return principal.getClubAuthorizationDTO();
	}


}
