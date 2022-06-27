package com.server.Puzzle.domain.board.dto.response;

import com.server.Puzzle.domain.board.enumType.Status;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class GetAllPostResponseDto {

    private Long boardId;
    private String title;
    private Status status;
    private LocalDateTime createDateTime;
    private String image_url;
    private String introduce;

}
