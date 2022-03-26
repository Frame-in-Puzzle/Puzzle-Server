package com.server.Puzzle.domain.oauth2.dto;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.enumType.Role;
import lombok.*;

import java.util.Collections;

@Builder
@Getter
public class UserProfile {

    private String oauthIdx;
    private String email;
    private String githubId;
    private String name;
    private String imageUrl;
    private String bio;
    private boolean isFirstVisited;

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