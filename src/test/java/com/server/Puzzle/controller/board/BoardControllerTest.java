package com.server.Puzzle.controller.board;

import com.google.gson.Gson;
import com.server.Puzzle.domain.board.controller.BoardController;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.dto.response.GetAllPostResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostResponseDto;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
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

    private static final String BASE_URI = "/api/board";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void 게시물_등록() throws Exception {
        // given
        doNothing().when(boardService)
                .post(any(PostRequestDto.class));

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
                        post(BASE_URI)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"))
                .andDo(print());
    }

    @Test
    void 이미지_주소_생성() throws Exception {
        // given
        doReturn("url").when(boardService)
                .createUrl(any(MultipartFile.class));

        final MockMultipartFile file = new MockMultipartFile("files",
                "PuzzleLogo.png",
                "image/png",
                new FileInputStream("src/test/resources/PuzzleLogo.png"));

        // when, then
        mockMvc.perform(
                        multipart(BASE_URI.concat("/create-url"))
                                .file(file).part(new MockPart("PuzzleLogo.png",file.getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("url"))
                .andDo(print());
    }

    @Test
    void 게시물_수정() throws Exception {
        // given
        doNothing().when(boardService)
                .correctionPost(any(Long.class), any(CorrectionPostRequestDto.class));

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
                    put(BASE_URI.concat("/{id}"),1L)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"))
                .andDo(print());
    }

    @Test
    void 게시물_전체_조회() throws Exception {
        // given
        final GetAllPostResponseDto response = GetAllPostResponseDto.builder()
                .boardId(1L)
                .title("title")
                .status(Status.RECRUITMENT)
                .createDateTime(LocalDateTime.now())
                .image_url("url")
                .introduce("hello")
                .build();

        doReturn(new PageImpl<>(List.of(response))).when(boardService)
                .getAllPost(any(Pageable.class));

        final String expectByTitle = "$..content[?(@.title == '%s')]";

        // when then
        mockMvc.perform(
                    get(BASE_URI.concat("/all"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(expectByTitle,"title").exists())
                .andDo(print());
    }

    @Test
    void 게시물_단일_조회() throws Exception {
        // given
        final GetPostResponseDto response = GetPostResponseDto.builder()
                .id(1L)
                .title("title")
                .contents("contents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .name("name")
                .githubId("githubId")
                .createdAt(LocalDateTime.now())
                .introduce("introduce")
                .fields(
                        List.of(Field.BACKEND)
                )
                .languages(
                        List.of(Language.JAVA)
                )
                .files(
                        List.of("url")
                )
                .build();

        doReturn(response).when(boardService)
                .getPost(1L);

        // when, then
        mockMvc.perform(
                    get(BASE_URI.concat("/{id}"),1L)
                )
                .andExpect(jsonPath("$.id",1).exists())
                .andExpect(jsonPath("$.title","title").exists())
                .andDo(print());
    }

    @Test
    void 게시물_삭제() throws Exception {
        // given
        doNothing().when(boardService)
                .deletePost(any(Long.class));

        // when, then
        mockMvc.perform(
                    delete(BASE_URI.concat("/{id}"),any(Long.class))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
