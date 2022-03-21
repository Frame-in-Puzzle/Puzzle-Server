package com.server.Puzzle.service.user;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.Impl.ProfileServiceImpl;
import com.server.Puzzle.domain.user.service.Impl.UserServiceImpl;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.enumType.Role;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.exception.ErrorCode;
import com.server.Puzzle.global.exception.collection.UserNotFoundException;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import com.server.Puzzle.global.util.CurrentUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.server.Puzzle.global.enumType.Language.SPRINGBOOT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrentUserUtil currentUserUtil;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    ProfileServiceImpl profileService;

    @Autowired
    EntityManager em;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void 로그인한_유저확인() {
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
                .isFirstVisited(true)
                .refreshToken("refreshToken")
                .githubId("honghyunin12")
                .build();

        userRepository.save(user);
        //when
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getGithubId(), "password", user.getRoles());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
    }

    @Test
    void 로그아웃() {
        userService.logout();

        User currentUser = currentUserUtil.getCurrentUser();

        User user = userRepository.findByName(currentUser.getName())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        assertEquals(user.getRefreshToken(), null);
    }

    @Test
    void 회원탈퇴() {
        User currentUser = currentUserUtil.getCurrentUser();

        userService.delete();

        assertEquals(userRepository.findByGithubId(currentUser.getGithubId()), Optional.empty());
    }
    
    @Test
    void 방문상태_확인() {
        User currentUser = currentUserUtil.getCurrentUser();

        assertEquals(currentUser.isFirstVisited(), true);

        UserUpdateDto user = UserUpdateDto.builder()
                .email("hyunin0102@gmail.com")
                .name("홍현인")
                .imageUrl("https://avatars.githubusercontent.com/u/68847615?v=4")
                .bio("상메")
                .field(Field.BACKEND)
                .language(List.of(Language.JAVA, SPRINGBOOT))
                .url("github.com/honghyunin")
                .build();

        profileService.profileUpdate(user);

        em.flush();
        em.clear();

        User user1 = userRepository.findByName(user.getName())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        assertEquals(user1.isFirstVisited(), false);
    }

    @Test
    void 리프레쉬_토큰_재발급() {
        User currentUser = currentUserUtil.getCurrentUser();

        String refreshToken = jwtTokenProvider.createRefreshToken();

        currentUser.updateRefreshToken(refreshToken);

        em.flush();
        em.clear();

        User user = userRepository.findByGithubId(currentUser.getGithubId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Map<String, String> map;

        map = userService.reissueToken(user.getRefreshToken());

        assertEquals(map.get("RefreshToken").substring(7), user.getRefreshToken());
    }

    @Test
    void 로그아웃_상태에서_리프레쉬_토큰_재발급() {
        User user = currentUserUtil.getCurrentUser();

        user.updateRefreshToken(null);

        em.flush();
        em.clear();

        String refreshToken = jwtTokenProvider.createRefreshToken();

        Map<String, String> map = null;

        assertThrows(CustomException.class, () -> {
            userService.reissueToken(refreshToken);
        });
    }
}
