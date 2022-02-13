package com.server.Puzzle.domain.board.repository;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    Page<Board> findBoardsByUser(User user, Pageable pageable);
}
