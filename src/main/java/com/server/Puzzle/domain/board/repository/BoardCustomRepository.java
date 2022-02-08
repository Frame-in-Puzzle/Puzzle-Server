package com.server.Puzzle.domain.board.repository;

import com.server.Puzzle.domain.board.domain.Board;

import java.util.List;

public interface BoardCustomRepository {
        List<Board> findByBoards();
}

