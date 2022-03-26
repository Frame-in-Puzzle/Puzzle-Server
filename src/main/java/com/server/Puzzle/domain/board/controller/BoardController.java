package com.server.Puzzle.domain.board.controller;

import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.dto.response.GetAllPostResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostByTagResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostResponseDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class BoardController {

    private final BoardService boardService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping
    public ResponseEntity<String> post(@RequestBody PostRequestDto request){
        boardService.post(request);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/create-url")
    public ResponseEntity<String> createUrl(@RequestPart MultipartFile files) {
        return ResponseEntity.ok(boardService.createUrl(files));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> correctionPost(@PathVariable("id") Long id, @RequestBody CorrectionPostRequestDto request){
        boardService.correctionPost(id, request);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/all")
    public ResponseEntity<Page<GetAllPostResponseDto>> getAllPost(@PageableDefault(size = 12) Pageable pageable) {
        return ResponseEntity.ok(boardService.getAllPost(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPostResponseDto> getPost(@PathVariable("id") Long id) {
        return ResponseEntity.ok(boardService.getPost(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id){
        boardService.deletePost(id);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<GetPostByTagResponseDto>> getPostByTag(@RequestParam Purpose purpose,
                                                                      @RequestParam List<Field> field,
                                                                      @RequestParam(defaultValue = "NULL") List<Language> language,
                                                                      @RequestParam Status status,
                                                                      @PageableDefault(size = 12) Pageable pageable)
    {
        return ResponseEntity.ok(boardService.getPostByTag(purpose, field, language, status, pageable));
    }

}
