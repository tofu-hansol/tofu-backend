package com.hansol.tofu.member;

import static com.hansol.tofu.member.enums.MemberStatus.*;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public Optional<MemberEntity> findMemberBy(String email) {
		return memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE);
	}

	public String getCurrentMemberEmail() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
	  		return principal.getUsername();
	}

}
