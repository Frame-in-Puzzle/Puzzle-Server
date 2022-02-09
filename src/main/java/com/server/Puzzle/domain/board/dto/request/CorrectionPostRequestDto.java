package com.server.Puzzle.domain.board.dto.request;

import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CorrectionPostRequestDto {

    private String title;
    private String contents;
    private Purpose purpose;
    private Status status;
    private List<String> fileUrl;

}
