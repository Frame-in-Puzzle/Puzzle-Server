package com.server.Puzzle.domain.board.service;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    Board post(List<MultipartFile> files, PostRequestDto request);
    Board correctionPost(Long id, CorrectionPostRequestDto request);
}
