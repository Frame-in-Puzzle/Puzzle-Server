package com.server.Puzzle.service.user;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.ProfileService;
import com.server.Puzzle.domain.user.service.TokenService;
import com.server.Puzzle.domain.user.service.UserService;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.enumType.Role;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.exception.ErrorCode;
import com.server.Puzzle.global.exception.collection.UserNotFoundException;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import com.server.Puzzle.global.util.CurrentUserUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.server.Puzzle.global.enumType.Language.SPRINGBOOT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Value("${security.jwt.token.secretKey}")
    private String secretKey;

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
    void 로그인한_유저확인() {
        //given
        User user = User.builder()
                .oauthIdx("68847615")
                .email("hyunin0102@gmail.com")
                .name("홍현인")
                .imageUrl("https://avatars.githubusercontent.com/u/68847615?v=4")
                .bio("한줄소개")
                .field(Field.BACKEND)
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
    void 토큰_재발급() {
        User user = userRepository.findByGithubId("honghyunin12")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);

        em.flush();
        em.clear();

        Map<String, String> map;

        map = tokenService.reissueToken(user.getRefreshToken(), user.getGithubId());

        assertEquals(map.get("RefreshToken").substring(7), user.getRefreshToken());
    }

    @Test
    void 로그아웃_상태에서_토큰_재발급() {
        User user = userRepository.findByGithubId("honghyunin12")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateRefreshToken(null);

        em.flush();
        em.clear();

        String refreshToken = jwtTokenProvider.createRefreshToken();

        assertThrows(CustomException.class, () -> {
            tokenService.reissueToken(refreshToken, "honghyunin12");
        });
    }

    @Test
    void 만료된_토큰으로_토큰_재발급() {

        String githubid = "honghyunin12";

        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(null);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(now)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        assertThrows(CustomException.class, () -> {
            tokenService.reissueToken(refreshToken, githubid);
        });
    }

    @Test
    void 유효하지_않은_토큰으로_재발급() {
        String githubid = "honghyunin12";

        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(null);
        Date validity = new Date(now.getTime() + 100L * 60);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey+"dfdffdf")
                .compact();

        assertThrows(CustomException.class, () -> {
            tokenService.reissueToken(refreshToken, githubid);
        });
    }
}
