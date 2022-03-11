package com.server.Puzzle.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.Puzzle.domain.board.controller.BoardController;
import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.security.SecurityConfig;
import com.server.Puzzle.global.security.jwt.JwtTokenFilter;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BoardController.class)
public class BoardControllerTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private ObjectMapper objectMapper;
//
//    @MockBean BoardService boardService;
//    @MockBean JwtTokenProvider jwtTokenProvider;
//
//    final GetPostResponseDto board = GetPostResponseDto.builder()
//            .id(1L)
//            .title("title")
//            .contents("contents")
//            .purpose(Purpose.PROJECT)
//            .status(Status.RECRUITMENT)
//            .fields(List.of(Field.BACKEND,Field.FRONTEND))
//            .languages(List.of(Language.JAVA,Language.TS))
//            .files(List.of("1234"))
//            .build();
//
//    @Disabled
//    @Test
//    void postTest() throws Exception{
//        // given
//        PostRequestDto request = PostRequestDto.builder().title("title")
//                .contents("contents")
//                .purpose(Purpose.PROJECT)
//                .status(Status.RECRUITMENT)
//                .fieldList(List.of(Field.BACKEND, Field.FRONTEND))
//                .languageList(List.of(Language.TS, Language.SPRINGBOOT))
//                .fileUrlList(List.of())
//                .build();
//
//        String content = objectMapper.writeValueAsString(request);
//
//        // when
//        mockMvc.perform(post("/v1/api/board/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//                        .content(content)
//                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLrhbjqsr3spIAiLCJhdXRoIjpbIlVTRVIiXSwiaWF0IjoxNjQ1MTY2NTU3LCJleHAiOjE2NDUxNzczNTd9.tsPDjRRrBgxpWr0a3MRgEKYoLIs9LmfTkLzO_UIIKnY"))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Disabled
//    @Test
//    void getPost() throws Exception{
//        Mockito.when(boardService.getPost(anyLong())).thenReturn(board);
//
//        mockMvc.perform(get("/v1/api/board/1"))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Disabled
//    @Test
//    void testController() throws Exception{
//        this.mockMvc.perform(get("/v1/api/board/test").accept(MediaType.TEXT_PLAIN))
//                .andExpect(status().isOk())
//                .andExpect(content().string("success"))
//                .andDo(print());
//    }
}
