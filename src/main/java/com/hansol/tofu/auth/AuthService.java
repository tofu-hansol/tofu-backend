package com.hansol.tofu.auth;

import com.hansol.tofu.auth.domain.dto.LoginRequestDTO;
import com.hansol.tofu.auth.domain.dto.LoginResponseDTO;
import com.hansol.tofu.auth.domain.dto.SignupRequestDTO;
import com.hansol.tofu.auth.filter.jwt.JwtTokenProvider;
import com.hansol.tofu.auth.filter.jwt.dto.JwtTokenDTO;
import com.hansol.tofu.clubmember.domain.dto.ClubAuthorizationDTO;
import com.hansol.tofu.clubmember.repository.ClubMemberRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.member.MemberService;
import com.hansol.tofu.member.domain.dto.MemberJoinRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hansol.tofu.error.ErrorCode.*;
import static com.hansol.tofu.member.enums.MemberStatus.ACTIVATE;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberService memberService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;
    private final ClubMemberRepository clubMemberRepository;

    public JwtTokenDTO refresh(String refreshToken) {

        String email = jwtTokenProvider.getEmailByToken(refreshToken);

        if (jwtTokenProvider.isTokenExpired(refreshToken)) {
            throw new BaseException(INVALID_TOKEN);
        }

        var memberEntity = memberService.findMemberBy(email, ACTIVATE)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        var refreshTokenEntity = jwtTokenProvider.findRefreshTokenBy(email)
                .orElseThrow(() -> new BaseException(NOT_FOUND_TOKEN));
        jwtTokenProvider.deleteRefreshTokenBy(refreshTokenEntity.getEmail());

        return jwtTokenProvider.createToken(memberEntity.getEmail(), memberEntity.getUserRole());
    }

    public Long signup(SignupRequestDTO signupRequestDTO) {
        var memberRequestDTO = MemberJoinRequestDTO.builder()
                .email(signupRequestDTO.email())
                .name(signupRequestDTO.name())
                .password(passwordEncoder.encode(signupRequestDTO.password()))
                .mbti(signupRequestDTO.mbti())
                .deptId(signupRequestDTO.deptId())
                .build();

        Long memberId = memberService.saveMember(memberRequestDTO);
        emailVerificationService.verifyEmail(memberRequestDTO.email());

        return memberId;
    }

    public void completeAccountVerification(String code) {
        String email = emailVerificationService.getEmailBy(code);
        var memberEntity = memberService.findMemberBy(email)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        memberEntity.completeSignUp();
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        var memberEntity = memberService.findMemberBy(loginRequestDTO.email())
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(loginRequestDTO.password(), memberEntity.getPassword())) {
            throw new BaseException(PASSWORD_MISMATCH);
        }

        if (!ACTIVATE.equals(memberEntity.getMemberStatus())) {
            throw new BaseException(NOT_ACTIVATE_MEMBER);
        }

        var jwtTokenDTO = jwtTokenProvider.createToken(memberEntity.getEmail(), memberEntity.getUserRole());
        return LoginResponseDTO.builder()
                .memberId(memberEntity.getId())
                .accessToken(jwtTokenDTO.accessToken())
                .refreshToken(jwtTokenDTO.refreshToken())
                .clubAuthorizationDTO(getClubAuthorityMap(memberEntity.getId()))
                .build();
    }

    private Map<Long, ClubAuthorizationDTO> getClubAuthorityMap(Long memberId) {
        return clubMemberRepository.findAllByMemberId(memberId).stream()
                .collect(Collectors.toMap(
                        clubMember -> clubMember.getClub().getId(),
                        clubMember -> ClubAuthorizationDTO.builder()
                                .clubId(clubMember.getClub().getId())
                                .clubName(clubMember.getClub().getName())
                                .clubRole(clubMember.getClubRole())
                                .build()
                ));
    }

}
