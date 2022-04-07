package com.server.Puzzle.domain.board.repository;

import com.server.Puzzle.domain.board.dto.response.GetPostByTagResponseDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardCustomRepository {

    Page<GetPostByTagResponseDto> findBoardByTag(Purpose purpose, List<Field> field, List<Language> language, Status status, Pageable pageable);

}
