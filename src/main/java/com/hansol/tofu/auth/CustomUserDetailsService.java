package com.hansol.tofu.auth;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.club.domain.dto.ClubAuth;
import com.hansol.tofu.club.repository.ClubMemberRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hansol.tofu.error.ErrorCode.NOT_FOUND_MEMBER;
import static com.hansol.tofu.member.enums.MemberStatus.ACTIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;
	private final ClubMemberRepository clubMemberRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		var member = memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var clubAuthList = clubMemberRepository.findAllByMemberId(member.getId())
				.stream().map(clubMember ->
					ClubAuth.builder()
							.clubId(clubMember.getClub().getId())
							.clubRole(clubMember.getClubRole())
							.build()
		).toList();

		return new CustomUserDetails(
				member.getEmail(),
				member.getPassword(),
				List.of(member.getUserRole().name()),
				clubAuthList
		);
	}
}
