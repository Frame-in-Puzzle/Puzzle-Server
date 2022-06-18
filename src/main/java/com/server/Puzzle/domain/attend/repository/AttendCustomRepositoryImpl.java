package com.server.Puzzle.domain.attend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.Puzzle.domain.attend.domain.Attend;
import com.server.Puzzle.domain.attend.dto.response.FindAllAttendResponse;
import com.server.Puzzle.domain.board.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.Puzzle.domain.attend.domain.QAttend.attend;
import static com.server.Puzzle.domain.board.domain.QBoard.board;


@RequiredArgsConstructor
@Repository
public class AttendCustomRepositoryImpl implements AttendCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FindAllAttendResponse> findAllByBoardId(Long boardId) {
        List<Attend> attends = jpaQueryFactory.selectFrom(attend)
                .where(attend.board.id.eq(boardId))
                .fetch();

        return attends.stream()
                .map(a -> new FindAllAttendResponse(
                        a.getId(),
                        a.getUser().getUserLanguages().stream()
                                .map(l -> l.getLanguage())
                                .collect(Collectors.toList()),
                        a.getUser().getName(),
                        a.getUser().getGithubId(),
                        a.getUser().getImageUrl(),
                        a.getAttendStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Board findBoardByAttendId(Long attendId) {
        Board result = jpaQueryFactory.select(board)
                .from(attend)
                .where(attend.id.eq(attendId))
                .fetchOne();

        return result;
    }

}
