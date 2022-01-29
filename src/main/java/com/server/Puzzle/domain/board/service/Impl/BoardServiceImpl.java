package com.server.Puzzle.domain.board.service.Impl;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CurrentUserUtil currentUserUtil;

    @Override
    public void post(PostRequestDto request) {
        String title = request.getTitle();
        String contents = request.getContents();
        Purpose purpose = request.getPurpose();
        Status status = request.getStatus();
        User currentUser = currentUserUtil.getCurrentUser();

        boardRepository.save(
                Board.builder()
                        .title(title)
                        .contents(contents)
                        .purpose(purpose)
                        .status(status)
                        .user(currentUser)
                        .build()
        );
    }
}
