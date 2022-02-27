package com.server.Puzzle.domain.attend.repository;

import com.server.Puzzle.domain.attend.dto.response.GetAllAttendResponse;

import java.util.List;

public interface AttendCustomRepository {

    List<GetAllAttendResponse> findAllByBoardId(Long boardId);
}
