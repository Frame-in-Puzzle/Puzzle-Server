package com.server.Puzzle.global.enumType;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{

    GUEST("ROLE_GUEST"),
    USER("ROLE_USER");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getAuthority() {
        return name();
    }

}
