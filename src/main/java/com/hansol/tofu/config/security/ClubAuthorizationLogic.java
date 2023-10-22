package com.hansol.tofu.config.security;

import com.hansol.tofu.auth.CustomUserDetailsService;
import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.club.enums.ClubRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("clubAuth")
@RequiredArgsConstructor
public class ClubAuthorizationLogic {
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * @param root     : Spring Security root Context
     * @param clubId   : 동호회 ID
     * @param clubRole : 동호회 최소 권한
     * @return            : 동호회 권한이 최소 권한보다 높거나 같으면 true, 아니면 false
     */
    public boolean decide(MethodSecurityExpressionOperations root, Long clubId, ClubRole clubRole) {
        var userPrincipal = (CustomUserDetails) root.getAuthentication().getPrincipal();
        if (isSufficientAuthority(userPrincipal, clubId, clubRole)) {
            return true;
        }
		refreshClubAuthority();
		return isSufficientAuthority(userPrincipal, clubId, clubRole);
    }

    private void refreshClubAuthority() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authentication.getName());

        var newAuthentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

    private boolean isSufficientAuthority(CustomUserDetails userPrincipal, Long clubId, ClubRole clubRole) {
        var clubAuthorizationMap = userPrincipal.getClubAuthorizationDTO();
        var clubAuthorizationDTO = Optional.ofNullable(clubAuthorizationMap.get(clubId));

        if (clubAuthorizationDTO.isEmpty()) {
            return false;
        } else {
            int findPriority = clubAuthorizationDTO.get().clubRole().getPriority();
            return findPriority <= clubRole.getPriority();
        }
    }
}
