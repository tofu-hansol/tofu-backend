package com.hansol.tofu.config.security;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.club.enums.ClubStatus;
import com.hansol.tofu.club.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("clubAuth")
@RequiredArgsConstructor
public class ClubAuthorizationLogic {

    private final ClubMemberRepository clubMemberRepository;

    public boolean decide(MethodSecurityExpressionOperations root, ClubStatus clubStatus) {
        var userPrincipal = (CustomUserDetails) root.getAuthentication().getPrincipal();
        var clubAuthorizationMap = userPrincipal.getClubAuthorizationMap();
//        clubAuthorizationList.stream().filter(clubAuth -> )


        boolean decision = root.hasAuthority("ADMIN");
        // custom work ...
        return decision;
    }

    public boolean decide2() {
        // ... authorization logic
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        var clubAuthorizationList = userPrincipal.getClubAuthorizationMap();


        return true;
    }
}