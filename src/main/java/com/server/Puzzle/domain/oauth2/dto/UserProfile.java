package com.server.Puzzle.domain.oauth2.dto;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.enumType.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;

@Getter
@Builder
public class UserProfile {
    private final String oauthId;
    private final String email;
    private final String name;
    private final String imageUrl;
    private final String bio;

    public User toUser() {
        return User.builder()
                .oauthId(oauthId)
                .email(email)
                .name(name)
                .imageUrl(imageUrl)
                .bio(bio)
                .roles(Collections.singletonList(Role.USER))
                .build();
    }
}