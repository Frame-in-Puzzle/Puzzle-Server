package com.server.Puzzle.domain.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String githubId;
    private String email;
    private Boolean isFirstVisited;
    private String accessToken;
    private String refreshToken;
}
