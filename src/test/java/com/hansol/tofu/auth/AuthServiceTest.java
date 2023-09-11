package com.hansol.tofu.auth;

import com.hansol.tofu.auth.domain.dto.SignupRequestDTO;
import com.hansol.tofu.auth.jwt.JwtTokenProvider;
import com.hansol.tofu.dept.domain.DeptEntity;
import com.hansol.tofu.member.MemberService;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthServiceTest {

    private JwtTokenProvider jwtTokenProvider;
    private AuthService sut;
    private MemberRepository memberRepository;


    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        memberRepository = mock(MemberRepository.class);
        sut = new AuthService(jwtTokenProvider, memberRepository);
    }

    @Test
    void signup_회원가입_요청에_성공한다() {
        // (1) 무엇을 테스트할 것인가 : 회원가입
        // (2) 어떤 상황에서 ? :
        // (3)	기대하는 결과는? : 로그인 성공하여 ACcess, Refresh 토큰 발급
        var signupRequestDTO = SignupRequestDTO.builder()
                .email("test@hansol.com")
                .password("test1234")
                .name("뭉치")
                .companyId(1L)
                .deptId(1L)
                .mbti("ENFJ")
                .build();
        var deptEntity = DeptEntity.builder().build();

        var memberEntity = signupRequestDTO.toEntity(signupRequestDTO, deptEntity);



        sut.signup(signupRequestDTO);



        verify(memberRepository).save(memberEntity);
    }


}