package com.server.Puzzle.domain.user.dto;

import com.server.Puzzle.global.enumType.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class MyBoardResponse {
    private String title;
    private Field field;
    private LocalDateTime date;
    private String contents;
    private String imageUrl;
}