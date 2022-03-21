package com.server.Puzzle.domain.user.service;

import com.server.Puzzle.domain.user.dto.UserUpdateDto;

import java.util.Map;

public interface UserService {
    void logout();
    void delete();
    void infoRegistration(UserUpdateDto userinfo);
    Map<String, String> reissuanceToken(String refreshToken);
}
