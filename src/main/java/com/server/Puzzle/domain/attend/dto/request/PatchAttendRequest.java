package com.server.Puzzle.domain.attend.dto.request;

import com.server.Puzzle.domain.attend.domain.AttendStatus;
import lombok.Getter;

@Getter
public class PatchAttendRequest {

    private Long attendId;
    private AttendStatus attendStatus;
}
