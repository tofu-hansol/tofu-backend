package com.hansol.tofu.member;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.clubmember.domain.dto.ClubAuthorizationDTO;
import com.hansol.tofu.dept.repository.DeptRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.global.SecurityUtils;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.domain.dto.MemberEditRequestDTO;
import com.hansol.tofu.member.domain.dto.MemberJoinRequestDTO;
import com.hansol.tofu.member.domain.dto.MemberMyProfileResponseDTO;
import com.hansol.tofu.member.domain.dto.MemberProfileResponseDTO;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.repository.MemberRepository;
import com.hansol.tofu.upload.image.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

import static com.hansol.tofu.error.ErrorCode.*;

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
        return memberRepository.findMemberByEmailAndMemberStatus(email, status);
    }

	@Transactional(readOnly = true)
	public Optional<MemberEntity> findMemberBy(Long memberId) {
		return memberRepository.findMemberById(memberId);
	}

	@Transactional(readOnly = true)
	public Optional<MemberEntity> findMemberBy(Long memberId, MemberStatus status) {
		return memberRepository.findMemberByIdAndMemberStatus(memberId, status);
	}

	// TODO: make unit test
	@Transactional(readOnly = true)
	public MemberMyProfileResponseDTO getMyProfile() {
		Long currentMemberId = SecurityUtils.getCurrentUserId();

		return memberRepository.findMyProfile(currentMemberId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
	}

	// TODO: make unit test
	@Transactional(readOnly = true)
	public MemberProfileResponseDTO getMemberProfile(Long memberId) {
		return memberRepository.findMemberProfile(memberId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
	}

    public Map<Long, ClubAuthorizationDTO> getCurrentMemberClubAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getClubAuthorizationDTO();
    }

    public Long saveMember(MemberJoinRequestDTO memberRequestDTO) {
        var deptEntity = deptRepository.findById(memberRequestDTO.deptId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_DEPT));

        if (memberRepository.existsMemberByEmail(memberRequestDTO.email())) {
            throw new BaseException(DUPLICATE_MEMBER);
        }

        return memberRepository.save(memberRequestDTO.toEntity(memberRequestDTO, deptEntity)).getId();
    }

	public Long editMemberProfile(MemberEditRequestDTO memberEditRequestDTO) {
        Long memberId = SecurityUtils.getCurrentUserId();
		var memberEntity = memberRepository.findById(memberId)
				.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var deptEntity = deptRepository.findById(memberEditRequestDTO.deptId())
				.orElseThrow(() -> new BaseException(NOT_FOUND_DEPT));

		memberEntity.changeMemberProfile(memberEditRequestDTO, deptEntity);

		return memberId;
	}

	public Long changeMemberProfileImage(MultipartFile profileImage) {
        Long memberId = SecurityUtils.getCurrentUserId();

		var memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		String imageUrl = storageService.uploadImage(profileImage, "images/member/");
		memberEntity.changeProfileImage(imageUrl);

		return memberId;
	}

}
