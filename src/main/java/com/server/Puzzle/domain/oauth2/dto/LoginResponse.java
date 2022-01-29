package com.server.Puzzle.domain.oauth2.dto;

import com.server.Puzzle.global.enumType.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private String name;
    private String email;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(String name, String email, String accessToken, String refreshToken) {
        this.name = name;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
