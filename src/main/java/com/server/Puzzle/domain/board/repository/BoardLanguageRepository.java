package com.server.Puzzle.domain.board.repository;

import com.server.Puzzle.domain.board.domain.BoardLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardLanguageRepository extends JpaRepository<BoardLanguage, Long> {

    void deleteByBoardId(Long id);

}
