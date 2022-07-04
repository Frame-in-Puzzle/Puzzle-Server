package com.server.Puzzle.domain.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.domain.BoardField;
import com.server.Puzzle.domain.board.domain.BoardFile;
import com.server.Puzzle.domain.board.domain.QBoard;
import com.server.Puzzle.domain.board.domain.BoardImage;
import com.server.Puzzle.domain.board.dto.response.GetPostByTagResponseDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.dto.UserBoardResponse;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.server.Puzzle.domain.board.domain.QBoard.board;
import static com.server.Puzzle.domain.board.domain.QBoardField.boardField;
import static com.server.Puzzle.domain.board.domain.QBoardFile.boardFile;
import static com.server.Puzzle.domain.board.domain.QBoardLanguage.boardLanguage;

@RequiredArgsConstructor
@Repository
public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<GetPostByTagResponseDto> findBoardByTag(Purpose purpose, List<Field> field, List<Language> language, Status status, Pageable pageable) {
        HashSet<Long> boardIdHashSet = new HashSet<>();

        if (field.get(0) == Field.ALL){
            List<Long> dbResult = jpaQueryFactory.from(boardField)
                    .select(boardField.board.id)
                    .fetch();

            boardIdHashSet.addAll(dbResult);
        } else {
            for (Field f : field) {
                String name = f.name();

                if(name.substring(name.length() - 3).equals("ALL")){
                    List<Long> dbresult = jpaQueryFactory.from(boardField)
                            .select(boardField.board.id)
                            .where(boardField.field.eq(Field.valueOf(name.substring(0,name.length() - 4))))
                            .fetch();

                    boardIdHashSet.addAll(dbresult);
                }
            }
        }

        for (Language l : language) {
            List<Long> dbREsult = jpaQueryFactory.from(boardLanguage)
                    .select(boardLanguage.board.id)
                    .where(boardLanguage.language.eq(l))
                    .fetch();

            boardIdHashSet.addAll(dbREsult);
        }

        QueryResults<Board> results;

        if (purpose.equals(Purpose.ALL)){
            if(status.equals(Status.ALL)){
                results = jpaQueryFactory.selectFrom(board)
                        .where(
                                board.id.in(boardIdHashSet)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
            } else {
                results = jpaQueryFactory.selectFrom(board)
                        .where(
                                board.id.in(boardIdHashSet),
                                board.status.eq(status)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
            }
        } else {
            if(status.equals(Status.ALL)){
                results = jpaQueryFactory.selectFrom(board)
                        .where(
                                board.id.in(boardIdHashSet),
                                board.purpose.eq(purpose)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
            } else {
                results = jpaQueryFactory.selectFrom(board)
                        .where(
                                board.id.in(boardIdHashSet),
                                board.status.eq(status),
                                board.purpose.eq(purpose)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
            }
        }

        List<GetPostByTagResponseDto> content = results.getResults().stream()
                .map(
                        b -> GetPostByTagResponseDto.builder()
                                .boardId(b.getId())
                                .title(b.getTitle())
                                .status(b.getStatus())
                                .createdDate(b.getCreatedDate())
                                .introduce(b.getIntroduce())
                                .thumbnail(b.getBoardImages().stream()
                                            .map(BoardImage::getImageUrl)
                                            .findFirst().orElse(null))
                        .build()
                ).collect(Collectors.toList());

        return new PageImpl<>(content, pageable, results.getTotal());
    }

    public Page<UserBoardResponse> findBoardsByUser(User user, Pageable pageable) {
        List<Board> boards = jpaQueryFactory.selectFrom(QBoard.board)
                .where(QBoard.board.user.eq(user))
                .innerJoin(QBoard.board.boardFields).fetchJoin()
                .fetch();

        String thumbnail = jpaQueryFactory.select(boardFile.url)
                .from(boardFile)
                .where(boardFile.board.eq(boards.stream().findFirst().get()))
                .fetchFirst();

        List<UserBoardResponse> userBoards = boards.stream()
                .map(b -> UserBoardResponse.builder()
                        .boardId(b.getId())
                        .title(b.getTitle())
                        .contents(b.getContents())
                        .introduce(b.getIntroduce())
                        .date(b.getCreatedDate())
                        .purpose(b.getPurpose())
                        .status(b.getStatus())
                        .thumbnail(thumbnail)
                        .fields(b.getBoardFields().stream()
                                .map(BoardField::getField)
                                .collect(Collectors.toList()))
                        .build()).collect(Collectors.toList());

        return new PageImpl<>(userBoards, pageable, boards.size());
    }

}
