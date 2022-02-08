package com.server.Puzzle.domain.board.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.repository.BoardCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> findByBoards() {
        return null;
    }
}
