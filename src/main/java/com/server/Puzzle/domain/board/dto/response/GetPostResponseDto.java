package com.server.Puzzle.domain.board.dto.response;

import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetPostResponseDto {

    private Long id;
    private String title;
    private String contents;
    private Purpose purpose;
    private Status status;
    private List<Field> fields;
    private List<Language> languages;
    private List<String> files;

}
