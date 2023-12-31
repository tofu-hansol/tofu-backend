package com.hansol.tofu.mock;

import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(
	factory = WithMockCustomUserSecurityContextFactory.class,
	setupBefore = TestExecutionEvent.TEST_EXECUTION
)
public @interface WithMockCustomUser {
    String username() default "lisa@test.com";

    String name() default "Lisa";
}
