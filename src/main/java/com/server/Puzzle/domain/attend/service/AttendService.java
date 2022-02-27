package com.server.Puzzle.domain.attend.service;

import com.server.Puzzle.domain.attend.dto.response.GetAllAttendResponse;

import java.util.List;

public interface AttendService {
    void requestAttend(Long boardId);

    List<GetAllAttendResponse> findAllAttend(Long boardId);
}
