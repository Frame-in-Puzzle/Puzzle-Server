package com.server.Puzzle.domain.user.service;

import com.server.Puzzle.domain.user.dto.UserUpdateDto;

public interface UserService {
    void logout();
    void delete();
    void infoRegister(UserUpdateDto userinfo);
}
