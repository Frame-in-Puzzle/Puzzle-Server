package com.server.Puzzle.domain.board.service;

import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.dto.response.GetAllPostResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostByTagResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostResponseDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    void post(PostRequestDto request);
    String createUrl(MultipartFile image);
    void correctionPost(Long boardId, CorrectionPostRequestDto request);
    Page<GetAllPostResponseDto> getAllPost(Pageable pageable);
    GetPostResponseDto getPost(Long boardId);
    void deletePost(Long boardId);
    Page<GetPostByTagResponseDto> getPostByTag(Purpose purpose, List<Field> field, List<Language> language, Status status, Pageable pageable);

}
