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

import static org.hamcrest.Matchers.notNullValue;
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
        Pageable pageable = PageRequest.of(0, 5);
        String githubId = "honghyunin";

        Page<UserBoardResponse> response = responseUserBoard(pageable);

        doReturn(response).when(profileService)
                .getUserBoard(githubId, pageable);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/{githubId}/board", githubId)
                        .queryParam("page", "0")
                        .queryParam("size", "5")
        );
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].boardId", notNullValue()))
                .andExpect(jsonPath("$.content[0].title", notNullValue()))
                .andExpect(jsonPath("$.content[0].date", notNullValue()))
                .andExpect(jsonPath("$.content[0].contents", notNullValue()))
                .andExpect(jsonPath("$.content[0].introduce", notNullValue()))
                .andExpect(jsonPath("$.content[0].status", notNullValue()))
                .andExpect(jsonPath("$.content[0].thumbnail", notNullValue()))
                .andExpect(jsonPath("$.content[0].fields", notNullValue()))
                .andExpect(jsonPath("$.content[0].purpose", notNullValue()));
    }

    @Test
    void 유저_프로필_조회_성공() throws Exception {
        String githubId = "honghyunin";
        UserProfileResponse response = getUserProfile();

        doReturn(response).when(profileService)
                .getProfile(githubId);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{githubId}", githubId));

        resultActions
                .andExpect(jsonPath("email", notNullValue()))
                .andExpect(jsonPath("name", notNullValue()))
                .andExpect(jsonPath("bio", notNullValue()))
                .andExpect(jsonPath("url", notNullValue()))
                .andExpect(jsonPath("field", notNullValue()))
                .andExpect(jsonPath("languages", notNullValue()))
                .andExpect(jsonPath("imageUrl", notNullValue()));
        ;
    }

    @Test
    void 유저_프로필_변경_성공() throws Exception {
        ProfileUpdateDto request = profileInfo();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/update")
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
