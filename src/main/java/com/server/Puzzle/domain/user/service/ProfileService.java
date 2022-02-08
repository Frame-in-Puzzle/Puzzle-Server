package com.server.Puzzle.domain.user.service;

import com.server.Puzzle.domain.user.dto.MyBoardResponse;
import com.server.Puzzle.domain.user.dto.UserInfoDto;

import java.util.List;

public interface ProfileService {
    UserInfoDto getProfile(String name);
    void profileUpdate(UserInfoDto userInfo);
    List<MyBoardResponse> getMyBoard();
}
