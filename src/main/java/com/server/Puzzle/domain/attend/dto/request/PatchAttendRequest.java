package com.server.Puzzle.domain.attend.dto.request;

import com.server.Puzzle.domain.attend.enumtype.AttendStatus;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PatchAttendRequest {

    private AttendStatus attendStatus;

}
