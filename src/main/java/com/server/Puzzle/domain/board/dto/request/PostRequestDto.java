package com.server.Puzzle.domain.board.dto.request;

import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import lombok.Getter;

@Getter
public class PostRequestDto {

    private String title;
    private String contents;
    private Purpose purpose;
    private Status status;

}