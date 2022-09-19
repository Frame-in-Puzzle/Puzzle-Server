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

/**
 * BoardController <br>
 * Puzzle 게시글 Controller
 */
@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시물 등록
     * @param request title, contents, purpose, status, introduce, fields, languages, imageUrls
     * @return ResponseEntity - Success
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping
    public ResponseEntity<String> post(@RequestBody PostRequestDto request){
        boardService.post(request);
        return ResponseEntity.ok("Success");
    }

    /**
     * 이미지 url 생성
     * @param image
     * @return imageUrl
     */
    @PostMapping("/create-url")
    public ResponseEntity<String> createUrl(@RequestPart MultipartFile image) {
        return ResponseEntity.ok(boardService.createUrl(image));
    }

    /**
     * 게시글 수정
     * @param boardId
     * @param request title, contents, purpose, status, introduce, imageUrls, fields, languages
     * @return ResponseEntity - Success
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/{boardId}")
    public ResponseEntity<String> correctionPost(@PathVariable("boardId") Long boardId, @RequestBody CorrectionPostRequestDto request){
        boardService.correctionPost(boardId, request);
        return ResponseEntity.ok("Success");
    }

    /**
     * 게시글 전체조회
     * @param pageable
     * @return List GetAllPostResponseDto - boardId, title, status, createDateTime, thumbnail, introduce
     */
    @GetMapping("/all")
    public ResponseEntity<Page<GetAllPostResponseDto>> getAllPost(@PageableDefault(size = 12) Pageable pageable) {
        return ResponseEntity.ok(boardService.getAllPost(pageable));
    }

    /**
     * 게시글 세부조회
     * @param boardId
     * @return GetPostResponseDto - id, title, contents, purpose, status, name, githubId, introduce, createDateTime, fields, languages, files
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<GetPostResponseDto> getPost(@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(boardService.getPost(boardId));
    }

    /**
     * 게시글 삭제
     * @param boardId
     * @return ResponseEntity - Success
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deletePost(@PathVariable("boardId") Long boardId){
        boardService.deletePost(boardId);
        return ResponseEntity.ok("Success");
    }

    /**
     * 게시글 태그 조회
     * @param purpose
     * @param fields
     * @param languages
     * @param status
     * @param pageable
     * @return GetPostByTagResponseDto - boardId, title, status, createdDate, thumbnail, introduce
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<GetPostByTagResponseDto>> getPostByTag(@RequestParam(defaultValue = "ALL") Purpose purpose,
                                                                      @RequestParam(defaultValue = "ALL") List<Field> fields,
                                                                      @RequestParam(defaultValue = "NULL") List<Language> languages,
                                                                      @RequestParam(defaultValue = "ALL") Status status,
                                                                      @PageableDefault(size = 12) Pageable pageable)
    {
        return ResponseEntity.ok(boardService.getPostByTag(purpose, fields, languages, status, pageable));
    }

}
