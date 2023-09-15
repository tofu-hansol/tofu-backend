package com.hansol.tofu.auth;

import com.hansol.tofu.auth.domain.dto.LoginRequestDTO;
import com.hansol.tofu.auth.filter.jwt.JwtTokenProvider;
import com.hansol.tofu.auth.filter.jwt.dto.JwtTokenDTO;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.member.MemberService;
import com.hansol.tofu.member.domain.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.hansol.tofu.error.ErrorCode.NOT_ACTIVATE_MEMBER;
import static com.hansol.tofu.error.ErrorCode.PASSWORD_MISMATCH;
import static com.hansol.tofu.member.enums.MemberStatus.ACTIVATE;
import static com.hansol.tofu.member.enums.MemberStatus.DORMANT;
import static com.hansol.tofu.member.enums.UserRole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    private AuthService sut;
    private JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;
    private EmailVerificationService emailVerificationService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        memberService = mock(MemberService.class);
        emailVerificationService = mock(EmailVerificationService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        sut = new AuthService(jwtTokenProvider, memberService, emailVerificationService, passwordEncoder);
    }

    @Test
    void login_존재하는_계정으로_로그인시_로그인에_성공한다() {
        var loginRequestDTO = LoginRequestDTO.builder()
                .email("test@hansol.com")
                .password("1234")
                .build();
        var memberEntity = MemberEntity.builder()
                .id(2L)
                .email(loginRequestDTO.email())
                .password(loginRequestDTO.password())
                .memberStatus(ACTIVATE)
                .userRole(ROLE_USER)
                .build();
        var jwtTokenDTO = JwtTokenDTO.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(memberService.findMemberBy(loginRequestDTO.email())).thenReturn(Optional.of(memberEntity));
        when(passwordEncoder.matches(loginRequestDTO.password(), memberEntity.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createToken(memberEntity.getEmail(), memberEntity.getUserRole())).thenReturn(jwtTokenDTO);


        var result = sut.login(loginRequestDTO);


        assertEquals(memberEntity.getId(), result.memberId());
        assertEquals(jwtTokenDTO.accessToken(), result.accessToken());
        assertEquals(jwtTokenDTO.refreshToken(), result.refreshToken());
    }

    @Test
    void login_비밀번호가_틀릴시_PASSWORD_MISMATCH_예외가_발생한다() {
        var loginRequestDTO = LoginRequestDTO.builder()
                .email("test@hansol.com")
                .password("1234")
                .build();
        var memberEntity = MemberEntity.builder()
                .id(2L)
                .email(loginRequestDTO.email())
                .password("2222")
                .memberStatus(ACTIVATE)
                .userRole(ROLE_USER)
                .build();

        when(memberService.findMemberBy(loginRequestDTO.email())).thenReturn(Optional.of(memberEntity));
        when(passwordEncoder.matches(loginRequestDTO.password(), memberEntity.getPassword())).thenReturn(false);


        var baseException = assertThrows(BaseException.class, () -> sut.login(loginRequestDTO));


        assertEquals(baseException.getMessage(), PASSWORD_MISMATCH.getMessage());
    }

    @Test
    void login_비활성화된_회원으로_로그인요청시_NOT_ACTIVATE_MEMBER_예외가_발생한다() {
        var loginRequestDTO = LoginRequestDTO.builder()
                .email("test@hansol.com")
                .password("1234")
                .build();
        var memberEntity = MemberEntity.builder()
                .id(2L)
                .email(loginRequestDTO.email())
                .password("1234")
                .memberStatus(DORMANT)
                .userRole(ROLE_USER)
                .build();

        when(memberService.findMemberBy(loginRequestDTO.email())).thenReturn(Optional.of(memberEntity));
        when(passwordEncoder.matches(loginRequestDTO.password(), memberEntity.getPassword())).thenReturn(true);


        var baseException = assertThrows(BaseException.class, () -> sut.login(loginRequestDTO));


        assertEquals(baseException.getMessage(), NOT_ACTIVATE_MEMBER.getMessage());
    }
}
