package com.server.Puzzle.domain.user.dto;

import com.server.Puzzle.domain.board.enumType.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class MyBoardResponse {
    private String title;
    private LocalDateTime date;
    private String contents;
    private Status status;
    private String files;
}