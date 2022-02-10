package com.server.Puzzle.domain.board.dto.response;

import com.server.Puzzle.domain.board.enumType.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetAllResponseDto {

    private String title;
    private Status status;
    private LocalDateTime createDateTime;
    private String image_url;

}
