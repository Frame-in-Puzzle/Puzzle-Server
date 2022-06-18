package com.server.Puzzle.domain.attend.dto.response;

import com.server.Puzzle.domain.attend.enumtype.AttendStatus;
import com.server.Puzzle.global.enumType.Language;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class FindAllAttendResponse {

    private Long id;
    private List<Language> languages;
    private String name;
    private String githubId;
    private String imageUrl;
    private AttendStatus attendStatus;

}
