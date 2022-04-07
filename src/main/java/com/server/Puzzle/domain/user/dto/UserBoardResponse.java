package com.server.Puzzle.domain.user.dto;

import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.global.enumType.Field;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserBoardResponse {

    private Long boardId;
    private String title;
    private LocalDateTime date;
    private String contents;
    private Status status;
    private String thumbnail;
    private List<Field> fields;
    private Purpose purpose;

}