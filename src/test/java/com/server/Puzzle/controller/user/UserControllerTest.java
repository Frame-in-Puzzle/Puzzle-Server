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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void 로그아웃_성공() throws Exception {

        doNothing().when(userService)
                .logout();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user/logout")
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 회원탈퇴_성공() throws Exception {
        doNothing().when(userService)
                .delete();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user/delete")
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 첫_방문_회원정보_등록_성공() throws Exception {
        UserUpdateDto userUpdateDto = requestUserUpdateDto();

        doNothing().when(userService)
                .infoRegistration(any(UserUpdateDto.class));

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(userUpdateDto))

        ).andDo(print());

        resultActions.andExpect(status().isOk());
    }

    private UserUpdateDto requestUserUpdateDto() {
        return UserUpdateDto.builder()
                .name("hyunin")
                .email("hyunin0102@gmail.com")
                .imageUrl("https://avatars.githubusercontent.com/u/68847615?v=4")
                .bio("i am bio")
                .field(Field.BACKEND)
                .languages(List.of(Language.SPRING, Language.SPRINGBOOT))
                .build();
    }
}