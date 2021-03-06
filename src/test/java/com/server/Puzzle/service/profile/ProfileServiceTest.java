package com.server.Puzzle.service.profile;

import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.dto.ProfileUpdateDto;
import com.server.Puzzle.domain.user.dto.UserBoardResponse;
import com.server.Puzzle.domain.user.dto.UserProfileResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    BoardService boardService;

    @BeforeEach
    void ??????_??????() {
        //given
        User user = User.builder()
                .oauthIdx("68847615")
                .email("hyunin0102@gmail.com")
                .name("?????????")
                .imageUrl("https://avatars.githubusercontent.com/u/68847615?v=4")
                .bio("????????????")
                .field(Field.BACKEND)
                .url("https://github.com/honghyunin")
                .isFirstVisited(true)
                .githubId("honghyunin12")
                .build();

        userRepository.save(user);

        createUserLanguage(user, JAVA, SPRINGBOOT);
        //when
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getGithubId(), "password", List.of(Role.ROLE_USER));

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
    void ?????????_??????() {
        String githubId = "honghyunin12";

        em.clear();

        UserProfileResponse user = profileService.getProfile(githubId);

        assertEquals("?????????", user.getName()); // name : hyunin
    }

    @Test
    void ?????????_??????() {
        ProfileUpdateDto user = ProfileUpdateDto.builder()
                .email("hyunin0102@gmail.com")
                .name("?????????")
                .bio("??????")
                .field(Field.BACKEND)
                .language(List.of(Language.JAVA, SPRINGBOOT))
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

    @Test
    void ??????_?????????_??????() {
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("title")
                .contents("contents")
                .introduce("hi i'm introduce")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .fieldList(List.of(Field.BACKEND, Field.FRONTEND))
                .languageList(List.of(Language.JAVA, Language.TS))
                .fileUrlList(List.of("google.com", "naver.com"))
                .build();

        boardService.post(postRequestDto);

        em.clear();

        Page<UserBoardResponse> board = profileService.getUserBoard("honghyunin12", Pageable.unpaged());

        assertEquals(board.map(UserBoardResponse::getContents).get().findFirst().get(), "contents");

        assertEquals(board.map(UserBoardResponse::getFields).get().findFirst().get(), postRequestDto.getFieldList());
        
    }
}
