package com.server.Puzzle.domain.board.controller;

import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.dto.response.GetAllResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetResponseDto;
import com.server.Puzzle.domain.board.service.BoardService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/v1/api/board")
@RestController
public class BoardController {

    private final BoardService boardService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @PostMapping("/")
    public void post(@RequestBody PostRequestDto request){
        boardService.post(request);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @PostMapping("/create-url")
    public String createUrl(@RequestPart MultipartFile files) {
        return boardService.createUrl(files);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @PutMapping("/{id}")
    public void correctionPost(@PathVariable("id") Long id, @RequestBody CorrectionPostRequestDto request){
        boardService.correctionPost(id, request);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @GetMapping
    public Page<GetAllResponseDto> getAllPost(@PageableDefault(size = 12) Pageable pageable) {
        return boardService.getAllPost(pageable);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @GetMapping("/{id}")
    public GetResponseDto getPost(@PathVariable("id") Long id) {
        return boardService.getPost(id);
    }
}
