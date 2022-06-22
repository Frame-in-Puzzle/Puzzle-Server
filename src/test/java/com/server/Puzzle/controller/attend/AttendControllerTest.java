package com.server.Puzzle.controller.attend;

import com.server.Puzzle.domain.attend.controller.AttendController;
import com.server.Puzzle.domain.attend.service.AttendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class AttendControllerTest {

    @InjectMocks
    private AttendController attendController;

    MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(attendController)
                .alwaysDo(print())
                .build();
    }

}
