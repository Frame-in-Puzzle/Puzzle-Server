package com.server.Puzzle.domain.user.repository;

import com.server.Puzzle.domain.user.dto.UserProfileResponse;

public interface UserCustomRepository {

    UserProfileResponse findByUser(String githubId);

}
