package com.hansol.tofu.member;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.club.domain.dto.ClubAuthorization;
import com.hansol.tofu.dept.repository.DeptRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.domain.MemberRequestDTO;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.hansol.tofu.error.ErrorCode.DUPLICATE_MEMBER;
import static com.hansol.tofu.error.ErrorCode.NOT_FOUND_DEPT;
import static com.hansol.tofu.member.enums.MemberStatus.ACTIVATE;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DeptRepository deptRepository;

    @Transactional(readOnly = true)
    public Optional<MemberEntity> findMemberBy(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<MemberEntity> findMemberBy(String email, MemberStatus status) {
        return memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE);
    }

    public List<ClubAuthorization> getCurrentMemberClubAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getClubAuthorizationMap();
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

    public Long saveMember(MemberRequestDTO memberRequestDTO) {
        var deptEntity = deptRepository.findById(memberRequestDTO.deptId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_DEPT));

        if (memberRepository.existsMemberByEmail(memberRequestDTO.email())) {
            throw new BaseException(DUPLICATE_MEMBER);
        }

        return memberRepository.save(memberRequestDTO.toEntity(memberRequestDTO, deptEntity)).getId();
    }

}
