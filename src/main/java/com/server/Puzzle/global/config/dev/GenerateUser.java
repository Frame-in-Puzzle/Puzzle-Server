package com.server.Puzzle.global.config.dev;

import com.server.Puzzle.domain.user.domain.Roles;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.repository.RolesRepository;
import com.server.Puzzle.domain.user.repository.UserLanguageRepository;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.enumType.Role;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.server.Puzzle.global.enumType.Language.*;

@Slf4j
@RequiredArgsConstructor
@Component
@Profile({"dev"})
public class GenerateUser {

    private final UserRepository userRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RolesRepository rolesRepository;

    @PostConstruct
    private void genereateUserAccount() {
        User hyunin = createHyuninAccount();
        User kyungjun = createKyungjunAccount();

        Roles roles = createRoles(hyunin);

        loggingAccess(roles, hyunin, kyungjun);
    }

    private User createHyuninAccount() {
        User user = userRepository.save(
                User.builder()
                        .id(1L)
                        .email("hyunin0102@gmail.com")
                        .name("hyunin")
                        .imageUrl("https://avatars.githubusercontent.com/u/68847615?v=4")
                        .githubId("honghyunin")
                        .bio("상메")
                        .isFirstVisited(false)
                        .field(Field.BACKEND)
                        .userLanguages(null)
                        .url("github.com/honghyunin")
                        .oauthIdx(null)
                        .refreshToken(null)
                        .build());

        rolesRepository.save(createRoles(user));

        List<Language> languages = new ArrayList<>();

        languages.add(JAVA);
        languages.add(SPRINGBOOT);

        createUserLanguage(languages, user);

        return user;
    }

    private User createKyungjunAccount() {
        User user = userRepository.save(
                User.builder()
                        .id(2L)
                        .email("developerjun0615@gmail.com")
                        .name("노경준")
                        .imageUrl("https://avatars.githubusercontent.com/u/68670670?v=4")
                        .githubId("KyungJunNoh")
                        .bio("상메")
                        .isFirstVisited(false)
                        .field(Field.BACKEND)
                        .userLanguages(null)
                        .url("github.com/KyungJunNoh")
                        .oauthIdx(null)
                        .refreshToken(null)
                        .build());

        rolesRepository.save(createRoles(user));

        List<Language> languages = new ArrayList<>();

        languages.add(JAVA);
        languages.add(SPRING);

        createUserLanguage(languages, user);

        return user;
    }

    private Roles createRoles(User user) {
        return Roles.builder()
                .role(Role.ROLE_USER)
                .user(user)
                .build();
    }

    private void createUserLanguage(List<Language> languages, User user) {
        for (Language language : languages) {
            userLanguageRepository.save(UserLanguage.builder()
                    .user(user)
                    .language(language)
                    .build());
        }
    }

    private void loggingAccess(Roles roles, User... users) {
        log.info("======================================= Access Token =======================================");
        for(User user: users) {
            user = User.builder()
                    .githubId(user.getGithubId())
                    .roles(Collections.singletonList(roles))
                    .build();
            log.info("{} {} : \"Bearer {}\"", user.getAuthorities(), user.getGithubId(), jwtTokenProvider.createToken(user.getGithubId(), user.getRoles()));
        }
        log.info("=============================================================================================");
    }

}