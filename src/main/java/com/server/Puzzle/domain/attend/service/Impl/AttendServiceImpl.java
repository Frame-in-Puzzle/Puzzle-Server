package com.server.Puzzle.domain.attend.service.Impl;

import com.server.Puzzle.domain.attend.domain.Attend;
import com.server.Puzzle.domain.attend.repository.AttendRepository;
import com.server.Puzzle.domain.attend.service.AttendService;
import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.exception.ErrorCode;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AttendServiceImpl implements AttendService {

    private final AttendRepository attendRepository;
    private final BoardRepository boardRepository;
    private final CurrentUserUtil currentUserUtil;

    @Override
    public void requestAttend(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        String githubId = currentUserUtil.getCurrentUser().getGithubId();

        if(board.isAttended(githubId)) throw new CustomException(ErrorCode.IS_ALREADY_ATTEND);

        attendRepository.save(
                Attend.builder()
                    .board(board)
                    .githubId(githubId)
                    .build()
        );
    }
}
