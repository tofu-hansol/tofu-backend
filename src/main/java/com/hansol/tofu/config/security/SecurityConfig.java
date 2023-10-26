package com.hansol.tofu.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.hansol.tofu.auth.CustomUserDetailsService;
import com.hansol.tofu.auth.filter.jwt.JwtAccessDeniedHandler;
import com.hansol.tofu.auth.filter.jwt.JwtAuthenticationEntryPoint;
import com.hansol.tofu.auth.filter.jwt.JwtAuthenticationFilter;
import com.hansol.tofu.auth.filter.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
		return auth.getAuthenticationManager();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.requestMatchers("/", "/promotion", "/api/auth/**", "/favicon.ico",
				"/images/**",
				"/api/depts/**", "/api/company/**",
				"/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html");
	}

	// https://docs.spring.io/spring-security/reference/servlet/authorization/architecture.html
	@Bean
	static RoleHierarchyImpl roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
		return roleHierarchy;
	}

	@Bean
	static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf((csrf) -> csrf.disable()).cors((cors) -> cors.disable());

		/*
			TODO: 추후 제거
			[구체적인 경로가 먼저 앞에 오며 그 뒤에는 그것보다 더 큰 범위의 경로가 온다]
			permitAll : 무조건 접근 허용
			anyRequest : 이외 요청에 대해
			authenticated : 인증된 사용자만 가능

			hasRole : 사용자 객체에 주어진 "ROLE_" prefix로 시작하는 권한
			hasAuthority : 사용자 객체에 주어진 권한
			--> 둘 차이는 앞에 "ROLE_" prefix가 붙는지 여부

			hasAnyRole : 사용자 객체에 주어진 "ROLE_" prefix로 시작하는 권한 중 하나라도 있는지 확인
			hasAnyAuthority : 사용자 객체에 주어진 권한 중 하나라도 있는지 확인
		 */
		http.authorizeHttpRequests(
			(authorize) -> authorize
				.requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
				.requestMatchers("/", "/promotion", "/favicon.ico", "/images/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/depts/**", "/api/company/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/clubs/**").permitAll()
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
					.anyRequest().authenticated()
		);

		http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService),
				UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(
				exceptionHandler -> exceptionHandler.authenticationEntryPoint(jwtAuthenticationEntryPoint)
					.accessDeniedHandler(jwtAccessDeniedHandler));

		return http.build();
	}
}
