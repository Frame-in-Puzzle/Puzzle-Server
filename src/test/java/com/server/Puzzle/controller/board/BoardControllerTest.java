package com.server.Puzzle.controller.board;

import com.google.gson.Gson;
import com.server.Puzzle.domain.board.controller.BoardController;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @Test
    void 게시물_등록() throws Exception {
        doNothing().when(boardService)
                .post(any(PostRequestDto.class));

        // given
        final String body = new Gson().toJson(PostRequestDto.builder()
                .title("title")
                .contents("contents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .introduce("this is board")
                .fieldList(List.of(Field.BACKEND, Field.FRONTEND))
                .languageList(List.of(Language.JAVA, Language.TS))
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .build());

        // when, then
        mockMvc.perform(
                        post("/api/board/")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"))
                .andDo(print());
    }

    @Test
    void 이미지_주소_생성() throws Exception {
        doReturn("url").when(boardService)
                .createUrl(any(MultipartFile.class));

        // given
        MockMultipartFile file = new MockMultipartFile("files",
                "PuzzleLogo.png",
                "image/png",
                new FileInputStream("src/test/resources/PuzzleLogo.png"));

        // when, then
        mockMvc.perform(
                        multipart("/api/board/create-url")
                                .file(file).part(new MockPart("PuzzleLogo.png",file.getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("url"))
                .andDo(print());
    }

    @Test
    void 게시물_수정() throws Exception {
        doNothing().when(boardService)
                .correctionPost(any(Long.class), any(CorrectionPostRequestDto.class));

        // given
        final String body = new Gson().toJson(CorrectionPostRequestDto.builder()
                .title("correctionTitle")
                .contents("correctionContents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .introduce("this is board")
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .languageList(List.of(Language.PYTORCH, Language.KOTLIN))
                .fieldList(List.of(Field.AI, Field.ANDROID))
                .build());

        // when, then
        mockMvc.perform(
                    put("/api/board/{id}",1L)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"))
                .andDo(print());
    }
}
