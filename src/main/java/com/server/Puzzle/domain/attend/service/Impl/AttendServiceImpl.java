package com.server.Puzzle.domain.attend.service.Impl;

import com.server.Puzzle.domain.attend.domain.Attend;
import com.server.Puzzle.domain.attend.dto.request.PatchAttendRequest;
import com.server.Puzzle.domain.attend.dto.response.GetAllAttendResponse;
import com.server.Puzzle.domain.attend.enumtype.AttendStatus;
import com.server.Puzzle.domain.attend.repository.AttendRepository;
import com.server.Puzzle.domain.attend.service.AttendService;
import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.enumType.IsAttendStatus;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.exception.ErrorCode;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

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

        User currentUser = currentUserUtil.getCurrentUser();

        if(board.isAttended(currentUser)) throw new CustomException(ErrorCode.IS_ALREADY_ATTEND);

        attendRepository.save(
                Attend.builder()
                        .board(board)
                        .user(currentUser)
                        .attendStatus(AttendStatus.WAIT)
                        .build()
        );
    }

    @Override
    public List<GetAllAttendResponse> getAllAttend(Long boardId) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        return attendRepository.findAllByBoardId(boardId);
    }

    @Transactional
    @Override
    public void patchAttend(Long attendId, PatchAttendRequest patchAttendRequest) {
        User currentUser = currentUserUtil.getCurrentUser();

        Board board = attendRepository.findBoardByAttendId(attendId);

        if (!board.isAuthor(currentUser)) throw new CustomException(ErrorCode.ATTEND_PATCH_PERMISSION_DENIED);

        board.updateAttendStatus(attendId, patchAttendRequest.getAttendStatus());
    }

    @Override
    public void deleteAttend(Long boardId) {
        User currentUser = currentUserUtil.getCurrentUser();

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        Long attendId = board.getAttends().stream()
                .filter(a -> a.isAttend(currentUser)).findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_ATTEND))
                .getId();

        attendRepository.deleteById(attendId);
    }

    @Override
    public IsAttendStatus checkAttendStatus(Long boardId) {
        User currentUser = currentUserUtil.getCurrentUser();
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        try{
            AttendStatus attendStatus = board.getAttends().stream()
                    .filter(a -> a.isAttend(currentUser))
                    .findFirst().get()
                    .getAttendStatus();

            switch (attendStatus){
                case WAIT:
                    return IsAttendStatus.CANT_CANCEL;
                case ACCEPT:
                case REFUSE:
                    return IsAttendStatus.CANT;
            }
        } catch (NoSuchElementException e){
            return IsAttendStatus.CAN;
        }

        return IsAttendStatus.CAN;
    }

}
