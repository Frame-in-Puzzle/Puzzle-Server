package com.server.Puzzle.controller.profile;

import com.google.gson.Gson;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.user.controller.ProfileController;
import com.server.Puzzle.domain.user.dto.ProfileUpdateDto;
import com.server.Puzzle.domain.user.dto.UserBoardResponse;
import com.server.Puzzle.domain.user.dto.UserProfileResponse;
import com.server.Puzzle.domain.user.service.ProfileService;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {

    private static final String URL = "/api/profile/";

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void 유저_작성글_조회_성공() throws Exception {
        final Pageable pageable = PageRequest.of(0, 5);
        final String githubId = "honghyunin";

        final Page<UserBoardResponse> response = responseUserBoard(pageable);

        doReturn(response).when(profileService)
                .getUserBoard(githubId, pageable);

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/{githubId}/board", githubId)
                        .queryParam("page", "0")
                        .queryParam("size", "5")
        );

        final String expectByTitle = "$..content[0].[?(@.title == '%s')]";

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(expectByTitle, "title").exists());
    }

    @Test
    void 유저_프로필_조회_성공() throws Exception {
        final String githubId = "honghyunin";
        final UserProfileResponse response = getUserProfile();

        doReturn(response).when(profileService)
                .getProfile(githubId);

        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{githubId}", githubId));

        final String expectByEmail = "$[?(@.email == '%s')]";

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(expectByEmail, "hyunin0102@gmail.com").exists());
    }

    @Test
    void 유저_프로필_변경_성공() throws Exception {
        final ProfileUpdateDto request = profileInfo();

        doNothing().when(profileService)
                .profileUpdate(any(ProfileUpdateDto.class));

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new Gson().toJson(request))
                        .characterEncoding("UTF-8")
        );

        resultActions.andExpect(status().isOk());
    }

    private ProfileUpdateDto profileInfo() {
        return ProfileUpdateDto.builder()
                .email("hyunin0102@gmail.com")
                .name("hyunin")
                .field(Field.BACKEND)
                .bio("bio")
                .language(List.of(Language.SPRING, Language.DJANGO))
                .build();
    }

    private UserProfileResponse getUserProfile() {
        return UserProfileResponse.builder()
                .email("hyunin0102@gmail.com")
                .name("hyunin")
                .bio("i am bio")
                .field(Field.BACKEND)
                .imageUrl("imageurl")
                .url("url")
                .languages(List.of(Language.SPRING, Language.SPRINGBOOT))
                .build();
    }

    private Page<UserBoardResponse> responseUserBoard(Pageable pageable) {
        UserBoardResponse boards = getUserBoards();
        return new PageImpl<>(List.of(boards), pageable, 1);
    }

    private UserBoardResponse getUserBoards() {
        return UserBoardResponse.builder()
                .boardId(1L)
                .title("title")
                .thumbnail("thumbnail")
                .fields(List.of(Field.BACKEND, Field.FRONTEND))
                .status(Status.ALL)
                .purpose(Purpose.PROJECT)
                .introduce("아이엠스튜디오")
                .contents("contents")
                .date(LocalDateTime.now())
                .build();
    }
}
