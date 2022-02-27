package com.server.Puzzle.global.dev_config;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
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
import java.util.List;

import static com.server.Puzzle.global.enumType.Language.*;
import static java.util.Collections.singletonList;

@Slf4j
@RequiredArgsConstructor
@Component
@Profile({"oauth"})
public class GenerateUser {

    private final UserRepository userRepository;
    private final UserLanguageRepository userLanguageRepo;
    private final JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    private void genereateUserAccount() {

        User hyunin = createHyuninAccount();
        User kyungjun = createKyungjunAccount();

        loggingAccess(hyunin, kyungjun);
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
                        .isFirstVisit(true)
                        .field(Field.BACKEND)
                        .userLanguages(null)
                        .url("github.com/honghyunin")
                        .roles(singletonList(Role.USER))
                        .oauthIdx(null)
                        .refreshToken(null)
                        .build());

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
                        .imageUrl("imageurl")
                        .githubId("KyungJunNoh")
                        .bio("상메")
                        .isFirstVisit(true)
                        .field(Field.BACKEND)
                        .userLanguages(null)
                        .url("github.com/KyungJunNoh")
                        .roles(singletonList(Role.USER))
                        .oauthIdx(null)
                        .refreshToken(null)
                        .build());

        List<Language> languages = new ArrayList<>();

        languages.add(JAVA);
        languages.add(SPRING);

        createUserLanguage(languages, user);
        return user;
    }

    private void createUserLanguage(List<Language> languages, User user) {

        for (Language language : languages) {
            userLanguageRepo.save(UserLanguage.builder()
                    .user(user)
                    .language(language)
                    .build());
        }
    }

    private void loggingAccess(User...users) {

        log.info("======================================= Access Token =======================================");
        for(User user: users) {
            log.info("{} {} : \"Bearer {}\"", user.getRoles(), user.getGithubId(), jwtTokenProvider.createToken(user.getGithubId(), user.getRoles()));
        }
        log.info("=============================================================================================");
    }
}