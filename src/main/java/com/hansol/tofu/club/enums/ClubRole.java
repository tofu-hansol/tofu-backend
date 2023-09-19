package com.hansol.tofu.club.enums;

import lombok.Getter;

@Getter
public enum ClubRole {

    PRESIDENT(1), MANAGER(2), MEMBER(3);

    private final int priority;

    ClubRole(int priority) {
        this.priority = priority;
    }
}
