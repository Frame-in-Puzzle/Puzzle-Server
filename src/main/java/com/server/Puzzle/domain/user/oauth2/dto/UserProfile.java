package com.server.Puzzle.domain.user.oauth2.dto;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.enumType.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfile {
    private final String email;
    private final String name;
    private final String imageUrl;
    private final String bio;

    @Builder
    public UserProfile(String email, String name, String imageUrl, String bio) {
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
        this.bio = bio;
    }

    public User toUser() {
        return User.builder()
                .email(email)
                .name(name)
                .imageUrl(imageUrl)
                .bio(bio)
                .roles(Role.USER)
                .build();
    }
}