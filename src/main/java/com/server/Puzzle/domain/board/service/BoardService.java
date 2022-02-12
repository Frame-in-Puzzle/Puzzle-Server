package com.server.Puzzle.domain.board.service;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.dto.response.GetAllPostResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
    Board post(PostRequestDto request);
    String createUrl(MultipartFile files);
    Board correctionPost(Long id, CorrectionPostRequestDto request);
    Page<GetAllPostResponseDto> getAllPost(Pageable pageable);
    GetPostResponseDto getPost(Long id);
    void deletePost(Long id);
}
