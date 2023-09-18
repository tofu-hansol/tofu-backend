package com.hansol.tofu.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hansol.tofu.auth.AuthService;
import com.hansol.tofu.auth.domain.dto.SignupRequestDTO;
import com.hansol.tofu.auth.jwt.dto.JwtTokenDTO;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.error.BaseExceptionHandler;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.hansol.tofu.error.ErrorCode.INVALID_TOKEN;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {
    private MockMvc client;
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        client = MockMvcBuilders.standaloneSetup(new AuthController(authService))
                .setControllerAdvice(new BaseExceptionHandler())
                .build();
    }

    @Test
    void signup_유효한정보로_회원가입요청시_성공한다() throws Exception {
        var signupRequestDTO = SignupRequestDTO.builder()
                .email("mch@hansol.com")
                .password("mch-pwd1234")
                .name("뭉치")
                .deptId(1L)
                .mbti("ISTJ")
                .build();

        client.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void signup_이메일형식에_맞지않을경우_400에러를_반환한다() throws Exception {
        var signupRequestDTO = SignupRequestDTO.builder()
                .email("aaaa.com")
                .password("mch-pwd1234")
                .name("뭉치")
                .deptId(1L)
                .mbti("ISTJ")
                .build();

        client.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("한솔그룹 이메일만 가능합니다")));
    }

    @Test
    void signup_한솔이메일이_아닐경우_400에러를_반환한다() throws Exception {
        var signupRequestDTO = SignupRequestDTO.builder()
                .email("mch@gmail.com")
                .password("mch-pwd1234")
                .name("뭉치")
                .deptId(1L)
                .mbti("ISTJ")
                .build();

        client.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("한솔그룹 이메일만 가능합니다")));
    }

    @Test
    void refresh_유효한_RefreshToken으로_토큰_재발급_요청_시_성공한다() throws Exception {
        String oldValidRefreshToken = "oldRefreshToken";
        when(authService.refresh(oldValidRefreshToken)).thenReturn(new JwtTokenDTO("90AccessToken", "90RefreshToken"));


        client.perform(post("/api/auth/refresh")
                        .header("RefreshToken", oldValidRefreshToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.refreshToken", is("90RefreshToken")))
                .andExpect(jsonPath("$.data.accessToken", is("90AccessToken")));

        verify(authService).refresh(oldValidRefreshToken);
    }

    @Test
    void refresh_유효하지_않는_RefreshToken으로_토큰_재발급_요청_시_401_에러를_반환한다() throws Exception {
        String oldValidRefreshToken = "oldRefreshToken";
        when(authService.refresh(oldValidRefreshToken)).thenThrow(new BaseException(INVALID_TOKEN));


        client.perform(post("/api/auth/refresh")
                        .header("RefreshToken", oldValidRefreshToken)
                )
                .andExpect(status().isUnauthorized());
    }
}