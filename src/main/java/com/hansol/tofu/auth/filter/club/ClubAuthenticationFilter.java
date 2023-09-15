//package com.hansol.tofu.auth.filter.club;
//
//import com.hansol.tofu.auth.CustomUserDetailsService;
//import com.hansol.tofu.club.annotation.filter.FilterMember;
//import com.hansol.tofu.club.repository.ClubMemberRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.method.HandlerMethod;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class ClubAuthenticationFilter extends OncePerRequestFilter {
//
//    private final ClubMemberRepository clubMemberRepository;
//    private final CustomUserDetailsService customUserDetailsService;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//		/*
//			요청 -> 총무만 가능한 Controller 메서드 요청
//		 */
//        // clubMemberRepository
////        if (((HandlerMethod) handler).getMethodAnnotation(FilterMember.class) == null) {
////            return true;
////        }
//
//
////		String bearerToken = request.getHeader("Authorization");
////		if (bearerToken != null && bearerToken.length() > 7) {
////			String accessToken = bearerToken.substring(7);
////			if (jwtTokenProvider.isTokenExpired(accessToken))
////				throw new BaseException(INVALID_TOKEN);
////
////			String email = jwtTokenProvider.getEmailByToken(accessToken);
////			UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
////ah
////			authenticateAndSetSecurityContext(userDetails);
////		}
//
//        filterChain.doFilter(request, response);
//    }
//
//}
