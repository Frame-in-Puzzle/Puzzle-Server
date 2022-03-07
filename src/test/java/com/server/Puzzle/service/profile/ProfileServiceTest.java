package com.server.Puzzle.service.profile;

import com.server.Puzzle.domain.user.dto.UserResponseDto;
import com.server.Puzzle.domain.user.service.Impl.ProfileServiceImpl;
import com.server.Puzzle.global.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class ProfileServiceTest {
    @Autowired
    ProfileServiceImpl profileService;

    @Test
    void 프로필_조회() {
        String githubId = "honghyunin";

        UserResponseDto user = profileService.getProfile(githubId);

        assertEquals("hyunin", user.getName()); // name : hyunin
    }
}
