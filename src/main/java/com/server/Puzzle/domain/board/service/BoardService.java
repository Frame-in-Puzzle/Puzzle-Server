package com.server.Puzzle.domain.board.service;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;

public interface BoardService {
    Board post(PostRequestDto request);
    Board correctionPost(Long id, CorrectionPostRequestDto request);
}
