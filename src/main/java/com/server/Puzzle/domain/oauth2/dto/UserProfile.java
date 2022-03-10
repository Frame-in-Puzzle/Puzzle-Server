package com.server.Puzzle.domain.oauth2.dto;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.enumType.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;

@Getter
@Builder
public class UserProfile {
    private final String oauthIdx;
    private final String email;
    private final String githubId;
    private final String name;
    private final String imageUrl;
    private final String bio;
    private final boolean isFirstVisited;

    public User toUser() {
        return User.builder()
                .oauthIdx(oauthIdx)
                .email(email)
                .name(name)
                .githubId(githubId)
                .imageUrl(imageUrl)
                .bio(bio)
                .roles(Collections.singletonList(Role.USER))
                .isFirstVisited(true)
                .build();
    }
}