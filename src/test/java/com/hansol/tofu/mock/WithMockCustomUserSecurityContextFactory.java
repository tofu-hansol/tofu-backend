package com.hansol.tofu.mock;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.club.domain.dto.ClubAuthorizationDTO;
import com.hansol.tofu.club.enums.ClubRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;
import java.util.Map;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        CustomUserDetails principal =
                new CustomUserDetails(
                        "lisa@test.com",
                        "test1234",
                        1L,
                        List.of("ROLE_USER"),
                        Map.of(
                                1L, ClubAuthorizationDTO.builder().clubId(1L).clubRole(ClubRole.PRESIDENT).build(),
                                2L, ClubAuthorizationDTO.builder().clubId(2L).clubRole(ClubRole.MANAGER).build(),
                                3L, ClubAuthorizationDTO.builder().clubId(3L).clubRole(ClubRole.MEMBER).build()
                        )
                );
        var authentication =
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
