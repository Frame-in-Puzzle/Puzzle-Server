package com.server.Puzzle.domain.board.dto.response;

import com.server.Puzzle.domain.board.enumType.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetPostByTagResponseDto {

    private Long boardId;
    private String title;
    private Status status;
    private LocalDateTime createdDate;
    private String fileUrl;

}