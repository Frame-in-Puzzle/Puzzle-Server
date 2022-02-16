package com.server.Puzzle.domain.user.service;

import com.server.Puzzle.domain.user.dto.MyBoardResponse;
import com.server.Puzzle.domain.user.dto.UserInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {
    UserInfoDto getProfile(String name);
    void profileUpdate(UserInfoDto userInfo);
    Page<MyBoardResponse> getMyBoard(String githubId, Pageable pageable);
}
