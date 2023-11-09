package com.hansol.tofu.config.security;

import com.hansol.tofu.auth.CustomUserDetailsService;
import com.hansol.tofu.auth.filter.jwt.JwtAccessDeniedHandler;
import com.hansol.tofu.auth.filter.jwt.JwtAuthenticationEntryPoint;
import com.hansol.tofu.auth.filter.jwt.JwtAuthenticationFilter;
import com.hansol.tofu.auth.filter.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
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

	// TODO(Reference) : https://docs.spring.io/spring-security/reference/servlet/authorization/architecture.html
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

		http.authorizeHttpRequests(
			(authorize) -> authorize
				.requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
				.requestMatchers("/", "/promotion", "/favicon.ico", "/images/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/depts/**", "/api/company/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/clubs/**", "/api/club-authority/**").permitAll()
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
