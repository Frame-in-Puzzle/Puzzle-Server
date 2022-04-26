package com.server.Puzzle.domain.user.service;

import com.server.Puzzle.domain.user.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    UserResponseDto getProfile(String name);
    void profileUpdate(ProfileUpdateDto profileUpdateDto);
    String profileImageUpdate(MultipartFile multipartFile);
    Page<UserBoardResponse> getUserBoard(String githubId, Pageable pageable);
}
