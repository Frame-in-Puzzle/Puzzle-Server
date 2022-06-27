package com.server.Puzzle.domain.board.dto.request;

import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class CorrectionPostRequestDto {

    private String title;
    private String contents;
    private Purpose purpose;
    private Status status;
    private String introduce;
    private List<String> fileUrls;
    private List<Field> fields;
    private List<Language> languages;

}
