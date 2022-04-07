package com.server.Puzzle.domain.oauth2.dto;

import com.server.Puzzle.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
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
                .isFirstVisited(true)
                .build();
    }

}