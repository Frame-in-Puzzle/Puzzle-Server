package com.server.Puzzle.domain.user.service;

import com.server.Puzzle.domain.user.dto.UserBoardResponse;
import com.server.Puzzle.domain.user.dto.UserResponseDto;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {

    UserResponseDto getProfile(String name);
    void profileUpdate(UserUpdateDto userInfo);
    Page<UserBoardResponse> getUserBoard(String githubId, Pageable pageable);

}
