package com.server.Puzzle.exception.user;

import com.server.Puzzle.domain.user.domain.Roles;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.repository.RolesRepository;
import com.server.Puzzle.domain.user.repository.UserLanguageRepository;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.TokenService;
import com.server.Puzzle.global.enumType.Role;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.exception.ErrorCode;
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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.server.Puzzle.global.enumType.Field.BACKEND;
import static com.server.Puzzle.global.enumType.Language.SPRINGBOOT;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class UserExceptionTest {

    @Value("${security.jwt.token.secretKey}")
    private String secretKey;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    TokenService tokenService;

    @Autowired
    CurrentUserUtil currentUserUtil;

    @Autowired
    UserLanguageRepository userLanguageRepository;

    @Autowired
    RolesRepository rolesRepository;

    @BeforeEach
    void 로그인한_유저확인() {
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

        UserLanguage userLanguage = UserLanguage.builder()
                .id(null)
                .language(SPRINGBOOT)
                .user(user)
                .build();

        Roles roles = Roles.builder()
                .id(null)
                .role(Role.ROLE_USER)
                .user(user)
                .build();

        userLanguageRepository.save(userLanguage);
        rolesRepository.save(roles);

        em.flush();
        em.clear();

        //when
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getGithubId(), "password", List.of(Role.ROLE_USER));

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
    }

    @Test
    void 로그아웃_상태에서_토큰_재발급() {
        String githubId = currentUserUtil.getCurrentUser().getGithubId();

        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateRefreshToken(null);

        em.flush();
        em.clear();

        String refreshToken = jwtTokenProvider.createRefreshToken();

        assertThrows(CustomException.class, () -> {
            tokenService.reissueToken(refreshToken, githubId);
        });
    }

    @Test
    void 만료된_토큰으로_토큰_재발급() {

        String githubId = currentUserUtil.getCurrentUser().getGithubId();

        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(null);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(now)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        assertThrows(CustomException.class, () -> {
            tokenService.reissueToken(refreshToken, githubId);
        });
    }

    @Test
    void 유효하지_않은_토큰으로_재발급() {
        String githubId = currentUserUtil.getCurrentUser().getGithubId();

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
            tokenService.reissueToken(refreshToken, githubId);
        });
    }

}
