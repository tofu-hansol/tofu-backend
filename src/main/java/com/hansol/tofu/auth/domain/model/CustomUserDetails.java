package com.hansol.tofu.auth.domain.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hansol.tofu.clubmember.domain.dto.ClubAuthorizationDTO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetails implements UserDetails {

	private String email;
	private String password;

	private Long memberId;
	private List<String> roles;
	private Map<Long, ClubAuthorizationDTO> clubAuthorizationDTO;

	public CustomUserDetails(String email, String password, Long memberId, List<String> roles,
		Map<Long, ClubAuthorizationDTO> clubAuthorizationDTO) {
		this.email = email;
		this.password = password;
		this.memberId = memberId;
		this.roles = roles;
		this.clubAuthorizationDTO = clubAuthorizationDTO;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
