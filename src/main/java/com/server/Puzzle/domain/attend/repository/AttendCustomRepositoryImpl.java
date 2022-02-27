package com.server.Puzzle.domain.attend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.Puzzle.domain.attend.domain.Attend;
import com.server.Puzzle.domain.attend.dto.response.GetAllAttendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.Puzzle.domain.attend.domain.QAttend.attend;


@RequiredArgsConstructor
@Repository
public class AttendCustomRepositoryImpl implements AttendCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetAllAttendResponse> findAllByBoardId(Long boardId) {
        List<Attend> attends = jpaQueryFactory.selectFrom(attend)
                .where(attend.board.id.eq(boardId))
                .fetch();

        return attends.stream()
                .map(a -> new GetAllAttendResponse(
                        a.getId(),
                        a.getLanguages().stream()
                                .map(l -> l.getLanguage())
                                .collect(Collectors.toList()),
                        a.getUser().getName(),
                        a.getUser().getGithubId(),
                        a.getUser().getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
