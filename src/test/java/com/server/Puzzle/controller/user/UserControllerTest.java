package com.server.Puzzle.controller.user;

import com.google.gson.Gson;
import com.server.Puzzle.domain.user.controller.UserController;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import com.server.Puzzle.domain.user.service.UserService;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String URL = "/api/user";
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    public void 로그아웃_성공() throws Exception {

        doNothing().when(userService)
                .logout();

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(URL + "/logout")
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 회원탈퇴_성공() throws Exception {
        doNothing().when(userService)
                .delete();

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(URL + "/delete")
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 첫_방문_회원정보_등록_성공() throws Exception {
        UserUpdateDto userUpdateDto = requestUserUpdateDto();

        doNothing().when(userService)
                .infoRegistration(any(UserUpdateDto.class));

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(URL + "/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(userUpdateDto))
                        .characterEncoding("UTF-8")
        );

        resultActions.andExpect(status().isOk());
    }

    private UserUpdateDto requestUserUpdateDto() {
        return UserUpdateDto.builder()
                .name("현인")
                .email("hyunin0102@gmail.com")
                .profileImageUrl("https://avatars.githubusercontent.com/u/68847615?v=4")
                .bio("i am bio")
                .field(Field.BACKEND)
                .languages(List.of(Language.SPRING, Language.SPRINGBOOT))
                .build();
    }
}