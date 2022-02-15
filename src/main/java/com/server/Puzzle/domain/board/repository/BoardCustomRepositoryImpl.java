package com.server.Puzzle.domain.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.response.GetPostByTagResponseDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.server.Puzzle.domain.board.domain.QBoard.board;
import static com.server.Puzzle.domain.board.domain.QBoardField.boardField;
import static com.server.Puzzle.domain.board.domain.QBoardLanguage.boardLanguage;


@RequiredArgsConstructor
@Repository
public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetPostByTagResponseDto> findBoardByTag(Purpose purpose, List<Field> field, List<Language> language, Status status, Pageable pageable) {
        HashSet<Long> boardIdHashSet = new HashSet<>();

        for (Field f : field) {
            String name = f.name();

            if(name.substring(name.length() - 3).equals("ALL")){
                List<Long> dbresult = jpaQueryFactory.from(boardField)
                        .select(boardField.board.id)
                        .where(boardField.field.eq(Field.valueOf(name.substring(0,name.length() - 4))))
                        .fetch();

                for (Long aLong : dbresult) {
                    System.out.println("[ Field ] 에서의 boardIDHashSet 값" + boardIdHashSet);
                    boardIdHashSet.add(aLong);
                }
            }
        }

        for (Language l : language) {
            List<Long> dbresult = jpaQueryFactory.from(boardLanguage)
                    .select(boardLanguage.board.id)
                    .where(boardLanguage.language.eq(l))
                    .fetch();

            for (Long aLong : dbresult) {
                System.out.println("[ Language ] 에서의 boardIDHashSet 값 = " + boardIdHashSet);
                boardIdHashSet.add(aLong);
            }
        }

        System.out.println("boardIdHashSet = " + boardIdHashSet);

        List<Board> boards = jpaQueryFactory.selectFrom(board)
                .where(
                        board.id.in(boardIdHashSet),
                        board.purpose.eq(purpose),
                        board.status.eq(status)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        System.out.println("boards = " + boards);

        return boards.stream()
                .map(b -> new GetPostByTagResponseDto(
                        b.getId(),
                        b.getTitle(),
                        b.getStatus(),
                        b.getCreatedDate(),
                        b.getBoardFiles().stream()
                                .map(f -> f.getUrl())
                                .findFirst().orElse(null)
                ))
                .collect(Collectors.toList());
    }

}