package com.server.Puzzle.domain.board.service;

import com.server.Puzzle.domain.board.dto.request.PostRequestDto;

public interface BoardService {
    void post(PostRequestDto request);
}
