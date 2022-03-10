package com.server.Puzzle.service.user;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.service.Impl.UserServiceImpl;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.global.enumType.Role;
import com.server.Puzzle.global.exception.ErrorCode;
import com.server.Puzzle.global.exception.collection.UserNotFoundException;
import com.server.Puzzle.global.util.CurrentUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrentUserUtil currentUserUtil;

    @Autowired
    UserServiceImpl userService;

    @DisplayName("로그인한 유저를 확인하는 테스트")
    @BeforeEach
    void getCurrentUserTest() {
        //given
        User user = User.builder()
                .oauthIdx("68847615")
                .email("hyunin0102@gmail.com")
                .name("홍현인")
                .imageUrl("https://avatars.githubusercontent.com/u/68847615?v=4")
                .bio("한줄소개")
                .field(Field.BACKEND)
                .roles(List.of(Role.USER))
                .url("https://github.com/honghyunin")
                .isFirstVisit(true)
                .refreshToken("refreshToken")
                .githubId("honghyunin12")
                .build();

        userRepository.save(user);
        //when
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getGithubId(), "password", user.getRoles());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);

        User currentUser = currentUserUtil.getCurrentUser();

        // then
        assertEquals("honghyunin12", currentUser.getGithubId());
    }

    @Test
    void 로그아웃_테스트() {
        userService.logout();

        User currentUser = currentUserUtil.getCurrentUser();

        User user = userRepository.findByName(currentUser.getName())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        assertEquals(user.getRefreshToken(), null);
    }
}
