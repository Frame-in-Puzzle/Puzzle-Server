package com.server.Puzzle.domain.board.service.Impl;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CurrentUserUtil currentUserUtil;

    @Override
    public Board post(PostRequestDto request) {
        String title = request.getTitle();
        String contents = request.getContents();
        Purpose purpose = request.getPurpose();
        Status status = request.getStatus();
        User currentUser = currentUserUtil.getCurrentUser();

        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .contents(contents)
                        .purpose(purpose)
                        .status(status)
                        .user(currentUser)
                        .build()
        );

        return board;
    }

    @Transactional
    @Override
    public Board correctionPost(Long id, CorrectionPostRequestDto request) {
        String title = request.getTitle();
        String contents = request.getContents();
        Purpose purpose = request.getPurpose();
        Status status = request.getStatus();

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        if(board.getUser() != currentUserUtil.getCurrentUser()) throw new IllegalArgumentException("게시물을 수정할 권한이 없습니다.");

        board
                .updateTitle(title)
                .updateContents(contents)
                .updatePurpose(purpose)
                .updateStatus(status);

        return board;
    }
}
