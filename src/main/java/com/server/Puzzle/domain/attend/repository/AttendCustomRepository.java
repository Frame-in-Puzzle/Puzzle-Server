package com.server.Puzzle.domain.attend.repository;

import com.server.Puzzle.domain.attend.dto.response.FindAllAttendResponse;
import com.server.Puzzle.domain.board.domain.Board;

import java.util.List;

public interface AttendCustomRepository {

    List<FindAllAttendResponse> findAllByBoardId(Long boardId);
    Board findBoardByAttendId(Long attendId);

}
