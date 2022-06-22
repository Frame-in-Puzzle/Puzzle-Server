package com.server.Puzzle.controller.attend;

import com.server.Puzzle.domain.attend.controller.AttendController;
import com.server.Puzzle.domain.attend.service.AttendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        doNothing().when(attendService)
                .requestAttend(any(Long.class));

        mockMvc.perform(
                    post("/api/attend/board/{boardId}",any(Long.class))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

}
