package com.server.Puzzle.domain.board.repository;

import com.server.Puzzle.domain.board.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardImage,Long> {

    List<BoardImage> findByBoardId(Long id);

}
