package com.server.Puzzle.controller.board;

import com.google.gson.Gson;
import com.server.Puzzle.domain.board.controller.BoardController;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.dto.response.GetAllPostResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostByTagResponseDto;
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
import java.io.IOException;
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

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .alwaysDo(print())
                .build();
    }

    private static final String BASE_URI = "/api/board";

    @Test
    void 게시물_등록() throws Exception {
        // given
        doNothing().when(boardService)
                .post(any(PostRequestDto.class));

        final String body = postBodyInfo();

        // when, then
        mockMvc.perform(
                        post(BASE_URI)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    void 이미지_주소_생성() throws Exception {
        // given
        doReturn("url").when(boardService)
                .createUrl(any(MultipartFile.class));

        final MockMultipartFile image = settingImage();

        // when, then
        mockMvc.perform(
                        multipart(BASE_URI.concat("/create-url"))
                                .file(image).part(new MockPart("PuzzleLogo.png",image.getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("url"));
    }

    @Test
    void 게시물_수정() throws Exception {
        // given
        doNothing().when(boardService)
                .correctionPost(any(Long.class), any(CorrectionPostRequestDto.class));

        final String body = correctionPostBodyInfo();

        // when, then
        mockMvc.perform(
                    put(BASE_URI.concat("/{boardId}"),1L)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    void 게시물_전체_조회() throws Exception {
        // given
        final GetAllPostResponseDto response = allPostResponse();

        doReturn(new PageImpl<>(List.of(response))).when(boardService)
                .getAllPost(any(Pageable.class));

        // when then
        mockMvc.perform(
                    get(BASE_URI.concat("/all"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title"));
    }

    @Test
    void 게시물_단일_조회() throws Exception {
        // given
        final GetPostResponseDto response = postResponse();

        doReturn(response).when(boardService)
                .getPost(1L);

        // when, then
        mockMvc.perform(
                    get(BASE_URI.concat("/{boardId}"),1L)
                )
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    void 게시물_삭제() throws Exception {
        // given
        doNothing().when(boardService)
                .deletePost(any(Long.class));

        // when, then
        mockMvc.perform(
                    delete(BASE_URI.concat("/{boardId}"),any(Long.class))
                )
                .andExpect(status().isOk());
    }

    @Test
    void 게시물_태그조회() throws Exception {
        // given
        final GetPostByTagResponseDto response = postByTagResponse();

        doReturn(new PageImpl<>(List.of(response)))
                .when(boardService).getPostByTag(
                        any(Purpose.class),
                        anyList(),
                        anyList(),
                        any(Status.class),
                        any(Pageable.class)
                );

        // when, then
        mockMvc.perform(
                    get(BASE_URI.concat("/filter"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title"));
    }

    private String postBodyInfo() {
        return new Gson().toJson(PostRequestDto.builder()
                .title("title")
                .contents("contents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .introduce("this is board")
                .fields(List.of(Field.BACKEND, Field.FRONTEND))
                .languages(List.of(Language.JAVA, Language.TS))
                .imageUrls(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .build());
    }

    private GetPostByTagResponseDto postByTagResponse() {
        return GetPostByTagResponseDto.builder()
                .boardId(1L)
                .title("title")
                .status(Status.RECRUITMENT)
                .createdDate(LocalDateTime.now())
                .thumbnail("url")
                .introduce("introduce")
                .build();
    }

    private MockMultipartFile settingImage() throws IOException {
        return new MockMultipartFile("image",
                "PuzzleLogo.png",
                "image/png",
                new FileInputStream("src/test/resources/PuzzleLogo.png"));
    }

    private String correctionPostBodyInfo() {
        return new Gson().toJson(CorrectionPostRequestDto.builder()
                .title("correctionTitle")
                .contents("correctionContents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .introduce("this is board")
                .imageUrls(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .languages(List.of(Language.PYTORCH, Language.KOTLIN))
                .fields(List.of(Field.AI, Field.ANDROID))
                .build());
    }

    private GetAllPostResponseDto allPostResponse() {
        return GetAllPostResponseDto.builder()
                .boardId(1L)
                .title("title")
                .status(Status.RECRUITMENT)
                .createDateTime(LocalDateTime.now())
                .thumbnail("url")
                .introduce("hello")
                .build();
    }

    private GetPostResponseDto postResponse() {
        return GetPostResponseDto.builder()
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
                .imageUrls(
                        List.of("url")
                )
                .build();
    }
}
