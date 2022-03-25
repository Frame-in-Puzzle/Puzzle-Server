package com.server.Puzzle.domain.user.service;

import java.util.Map;

public interface TokenService {
    Map<String, String> reissueToken(String refreshToken, String githubId);
}
