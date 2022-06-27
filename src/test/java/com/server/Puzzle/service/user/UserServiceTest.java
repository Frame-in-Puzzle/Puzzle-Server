package com.server.Puzzle.service.user;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.dto.ProfileUpdateDto;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.ProfileService;
import com.server.Puzzle.domain.user.service.TokenService;
import com.server.Puzzle.domain.user.service.UserService;
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
import java.util.stream.Collectors;

import static com.server.Puzzle.global.enumType.Field.BACKEND;
import static com.server.Puzzle.global.enumType.Language.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrentUserUtil currentUserUtil;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    ProfileService profileService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    EntityManager em;

    @BeforeEach
    void 유저_세팅() {
        //given
        User user = User.builder()
                .oauthIdx("68847615")
                .email("hyunin0102@gmail.com")
                .name("홍현인")
                .profileImageUrl("https://avatars.githubusercontent.com/u/68847615?v=4")
                .bio("한줄소개")
                .field(BACKEND)
                .url("https://github.com/honghyunin")
                .isFirstVisited(true)
                .refreshToken("refreshToken")
                .githubId("honghyunin12")
                .build();

        userRepository.save(user);
        //when
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getGithubId(), "password", List.of(Role.ROLE_USER));

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

        em.flush();
        em.clear();

        assertEquals(userRepository.findByGithubId(currentUser.getGithubId()), Optional.empty());
    }
    
    @Test
    void 방문상태_확인() {
        User currentUser = currentUserUtil.getCurrentUser();

        assertEquals(currentUser.isFirstVisited(), true);

        ProfileUpdateDto user = ProfileUpdateDto.builder()
                .email("hyunin0102@gmail.com")
                .name("홍현인")
                .bio("상메")
                .field(BACKEND)
                .languages(List.of(JAVA, SPRINGBOOT))
                .build();

        profileService.profileUpdate(user);

        em.flush();
        em.clear();

        User user1 = userRepository.findByName(user.getName())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        assertEquals(user1.isFirstVisited(), false);
    }

    @Test
    void 토큰_재발급() {
        String githubId = currentUserUtil.getCurrentUser().getGithubId();

        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);

        em.flush();
        em.clear();

        Map<String, String> map = tokenService.reissueToken(user.getRefreshToken(), user.getGithubId());

        User findUser = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        assertEquals(map.get("RefreshToken").substring(7), findUser.getRefreshToken());
    }

    @Test
    void 첫_방문_시_정보등록() {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name("인현홍")
                .profileImageUrl("imageUrl")
                .bio("bio")
                .languages(List.of(TS, REACT))
                .field(BACKEND)
                .email("s20080@gsm.hs.kr")
                .build();

        userService.infoRegistration(userUpdateDto);

        em.flush();
        em.clear();

        User user = userRepository.findByName(userUpdateDto.getName()).orElseThrow();

        assertEquals(userUpdateDto.getName(), user.getName());
        assertEquals(userUpdateDto.getLanguages(), user.getUserLanguages().stream().map(UserLanguage::getLanguage).collect(Collectors.toList()));
    }

}
