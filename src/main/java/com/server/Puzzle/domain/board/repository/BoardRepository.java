package com.server.Puzzle.domain.board.repository;

import com.server.Puzzle.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long>, BoardCustomRepository {
}
