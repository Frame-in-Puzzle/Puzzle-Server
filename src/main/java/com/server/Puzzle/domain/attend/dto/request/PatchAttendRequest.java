package com.server.Puzzle.domain.attend.dto.request;

import com.server.Puzzle.domain.attend.domain.AttendStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PatchAttendRequest {

    private Long attendId;
    private AttendStatus attendStatus;
}
