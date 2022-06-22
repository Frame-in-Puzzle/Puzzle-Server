package com.server.Puzzle.controller.attend;

import com.google.gson.Gson;
import com.server.Puzzle.domain.attend.controller.AttendController;
import com.server.Puzzle.domain.attend.dto.request.PatchAttendRequest;
import com.server.Puzzle.domain.attend.dto.response.FindAllAttendResponse;
import com.server.Puzzle.domain.attend.enumtype.AttendStatus;
import com.server.Puzzle.domain.attend.service.AttendService;
import com.server.Puzzle.domain.board.enumType.IsAttendStatus;
import com.server.Puzzle.global.enumType.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AttendControllerTest {

    @InjectMocks
    private AttendController attendController;

    @Mock
    private AttendService attendService;

    MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(attendController)
                .alwaysDo(print())
                .build();
    }

    @Test
    void 참가요청() throws Exception {
        // given
        doNothing().when(attendService)
                .requestAttend(any(Long.class));

        // when, then
        mockMvc.perform(
                    post("/api/attend/board/{boardId}",any(Long.class))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    void 참가요청목록_전체조회() throws Exception {
        // given
        FindAllAttendResponse response = findAllAttendResponse();

        doReturn(List.of(response)).when(attendService)
                .findAllAttend(any(Long.class));

        // when, then
        mockMvc.perform(
                    get("/api/attend/board/{boardId}",1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].languages").value("JAVA"))
                .andExpect(jsonPath("$[0].name").value("노경준"))
                .andExpect(jsonPath("$[0].githubId").value("KyungJunNoh"))
                .andExpect(jsonPath("$[0].imageUrl").value("url"))
                .andExpect(jsonPath("$[0].attendStatus").value("WAIT"));
    }

    @Test
    void 참가요청_상태_수정() throws Exception {
        // given
        doNothing().when(attendService)
                        .patchAttend(any(Long.class),any(PatchAttendRequest.class));

        String body = patchAttendBody();

        // when, then
        mockMvc.perform(
                    patch("/api/attend/{attendId}", 1L)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    void 참가요청_취소() throws Exception {
        // given
        doNothing().when(attendService)
                .deleteAttend(any(Long.class));

        // when, then
        mockMvc.perform(
                    delete("/api/attend/board/{boardId}", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    void 참가요청_상태확인() throws Exception {
        // given
        doReturn(IsAttendStatus.CAN).when(attendService)
                .checkAttendStatus(any(Long.class));

        // when, then
        mockMvc.perform(
                        get("/api/attend/status/board/{boardId}", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("\"CAN\""));
    }

    private FindAllAttendResponse findAllAttendResponse() {
        return FindAllAttendResponse.builder()
                .id(1L)
                .languages(List.of(Language.JAVA))
                .name("노경준")
                .githubId("KyungJunNoh")
                .imageUrl("url")
                .attendStatus(AttendStatus.WAIT)
                .build();
    }

    private String patchAttendBody() {
        return new Gson().toJson(PatchAttendRequest.builder()
                .attendStatus(AttendStatus.ACCEPT)
                .build());
    }

}
