package com.server.Puzzle.domain.attend.service.Impl;

import com.server.Puzzle.domain.attend.domain.Attend;
import com.server.Puzzle.domain.attend.domain.AttendLanguage;
import com.server.Puzzle.domain.attend.dto.response.GetAllAttendResponse;
import com.server.Puzzle.domain.attend.repository.AttendLanguageRepository;
import com.server.Puzzle.domain.attend.repository.AttendRepository;
import com.server.Puzzle.domain.attend.service.AttendService;
import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.exception.ErrorCode;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AttendServiceImpl implements AttendService {

    private final AttendRepository attendRepository;
    private final AttendLanguageRepository attendLanguageRepository;
    private final BoardRepository boardRepository;
    private final CurrentUserUtil currentUserUtil;

    @Override
    public void requestAttend(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        User currentUser = currentUserUtil.getCurrentUser();

        if(board.isAttended(currentUser)) throw new CustomException(ErrorCode.IS_ALREADY_ATTEND);

        Attend attend = attendRepository.save(
                Attend.builder()
                        .board(board)
                        .user(currentUser)
                        .build()
        );

        for (UserLanguage userLanguage : currentUser.getUserLanguages()) {
            attendLanguageRepository.save(
                    AttendLanguage.builder()
                            .attend(attend)
                            .language(userLanguage.getLanguage())
                            .build()
            );
        }

    }

    @Override
    public List<GetAllAttendResponse> findAllAttend(Long boardId) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        return attendRepository.findAllByBoardId(boardId);
    }
}
