package com.server.Puzzle.domain.user.oauth2.dto;

import com.server.Puzzle.global.enumType.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private List<Role> roles;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private String bio;

    @Builder
    public LoginResponse(Long id, String name, String email, String imageUrl, List<Role> roles, String tokenType, String accessToken, String refreshToken, String bio) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.roles = roles;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
