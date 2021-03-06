package com.server.Puzzle.domain.attend.service;

import com.server.Puzzle.domain.attend.dto.request.PatchAttendRequest;
import com.server.Puzzle.domain.attend.dto.response.FindAllAttendResponse;
import com.server.Puzzle.domain.board.enumType.IsAttendStatus;

import java.util.List;

public interface AttendService {

    void requestAttend(Long boardId);

    List<FindAllAttendResponse> findAllAttend(Long boardId);

    void patchAttend(Long attendId, PatchAttendRequest patchAttendRequest);

    void deleteAttend(Long boardId);

    IsAttendStatus checkAttendStatus(Long boardId);
}
