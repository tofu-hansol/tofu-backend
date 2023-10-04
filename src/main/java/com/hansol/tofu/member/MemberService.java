package com.hansol.tofu.member;

import static com.hansol.tofu.error.ErrorCode.*;
import static com.hansol.tofu.member.enums.MemberStatus.*;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.club.domain.dto.ClubAuthorizationDTO;
import com.hansol.tofu.dept.repository.DeptRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.domain.dto.MemberEditRequestDTO;
import com.hansol.tofu.member.domain.dto.MemberJoinRequestDTO;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.repository.MemberRepository;
import com.hansol.tofu.upload.image.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DeptRepository deptRepository;
	private final StorageService storageService;

    @Transactional(readOnly = true)
    public Optional<MemberEntity> findMemberBy(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<MemberEntity> findMemberBy(String email, MemberStatus status) {
        return memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE);
    }

    public Map<Long, ClubAuthorizationDTO> getCurrentMemberClubAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getClubAuthorizationDTO();
    }

//    public void refreshClubAuthority() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        auth.getDetails();
//        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
//        updatedAuthorities.add(...); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]
//
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
//
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//    }


    public String getCurrentMemberEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    public Long saveMember(MemberJoinRequestDTO memberRequestDTO) {
        var deptEntity = deptRepository.findById(memberRequestDTO.deptId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_DEPT));

        if (memberRepository.existsMemberByEmail(memberRequestDTO.email())) {
            throw new BaseException(DUPLICATE_MEMBER);
        }

        return memberRepository.save(memberRequestDTO.toEntity(memberRequestDTO, deptEntity)).getId();
    }

	public Long editMember(Long memberId, MemberEditRequestDTO memberEditRequestDTO) {
		// 회원 ID를 통해 회원 엔티티를 가져온다.
		var memberEntity = memberRepository.findById(memberId)
				.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var deptEntity = deptRepository.findById(memberEditRequestDTO.deptId())
				.orElseThrow(() -> new BaseException(NOT_FOUND_DEPT));

		// 회원 엔티티의 change 메서드를 이용하여 정보를 수정한다.
		memberEntity.changeMemberProfile(memberEditRequestDTO, deptEntity);

		return memberId;
	}

	public Long changeMemberProfileImage(Long memberId, MultipartFile profileImage) {
		// 회원 ID를 통해 회원 엔티티를 가져온다.
		var memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		String imageUrl = storageService.uploadImage(profileImage, "images/member/");

		// 회원 엔티티의 change 메서드를 이용하여 정보를 수정한다.
		memberEntity.changeProfileImage(imageUrl);

		return memberId;
	}

}
