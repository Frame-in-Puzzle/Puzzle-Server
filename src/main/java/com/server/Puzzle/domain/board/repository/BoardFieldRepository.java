package com.server.Puzzle.domain.board.repository;

import com.server.Puzzle.domain.board.domain.BoardField;
import com.server.Puzzle.domain.board.domain.BoardLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFieldRepository extends JpaRepository<BoardField, Long> {
}
