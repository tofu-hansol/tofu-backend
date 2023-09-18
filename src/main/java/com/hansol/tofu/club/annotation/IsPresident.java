package com.hansol.tofu.club.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@clubAuth.decide(#root, #clubId, T(com.hansol.tofu.club.enums.ClubRole).PRESIDENT.name())")
public @interface IsPresident {
}
