package com.server.Puzzle.service.profile;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.dto.UserResponseDto;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import com.server.Puzzle.domain.user.repository.UserLanguageRepository;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.ProfileService;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.enumType.Role;
import com.server.Puzzle.global.util.CurrentUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.server.Puzzle.global.enumType.Language.JAVA;
import static com.server.Puzzle.global.enumType.Language.SPRINGBOOT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class ProfileServiceTest {
    @Autowired
    CurrentUserUtil currentUserUtil;

    @Autowired
    ProfileService profileService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserLanguageRepository langRepo;

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
                .roles(List.of(Role.USER))
                .url("https://github.com/honghyunin")
                .isFirstVisited(true)
                .githubId("honghyunin12")
                .build();

        userRepository.save(user);

        createUserLanguage(user, JAVA, SPRINGBOOT);
        //when
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getGithubId(), "password", user.getRoles());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
    }

    private void createUserLanguage(User user, Language...languages) {
        for(Language l: languages) {
            langRepo.save(
                    UserLanguage.builder()
                            .user(user)
                            .language(l)
                            .build()
            );
        }
    }

    @Test
    void 프로필_조회() {
        String githubId = "honghyunin12";

        UserResponseDto user = profileService.getProfile(githubId);

        assertEquals("홍현인", user.getName()); // name : hyunin
    }

    @Test
    void 프로필_수정() {
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

        User savedUser = currentUserUtil.getCurrentUser();

        assertEquals(user.getEmail(), savedUser.getEmail());

        assertEquals(user.getField(), savedUser.getField());

        assertEquals(user.getLanguage(), savedUser.getUserLanguages().stream().
                map(u -> u.getLanguage()).collect(Collectors.toList()));
    }
}
