package com.server.Puzzle.domain.user.repository;

import com.server.Puzzle.domain.user.dto.UserResponseDto;

public interface UserCustomRepository {
    UserResponseDto findByUser(String githubId);
}
