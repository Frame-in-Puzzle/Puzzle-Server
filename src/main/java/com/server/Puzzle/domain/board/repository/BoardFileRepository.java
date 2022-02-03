package com.server.Puzzle.domain.board.repository;

import com.server.Puzzle.domain.board.domain.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile,Long> {
}
