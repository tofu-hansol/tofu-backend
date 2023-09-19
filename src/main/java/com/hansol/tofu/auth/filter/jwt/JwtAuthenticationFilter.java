package com.hansol.tofu.auth.filter.jwt;

import com.hansol.tofu.auth.CustomUserDetailsService;
import com.hansol.tofu.error.BaseException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.hansol.tofu.error.ErrorCode.INVALID_TOKEN;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.length() > 7) {
			String accessToken = bearerToken.substring(7);
			if (jwtTokenProvider.isTokenExpired(accessToken))
				throw new BaseException(INVALID_TOKEN);

			String email = jwtTokenProvider.getEmailByToken(accessToken);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

			authenticateAndSetSecurityContext(userDetails);
		}

		filterChain.doFilter(request, response);
	}

	private void authenticateAndSetSecurityContext(UserDetails userDetails) {
		var authentication = new UsernamePasswordAuthenticationToken(
			userDetails,
			null,
			userDetails.getAuthorities()
		);

		var context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

	}

}
