package com.server.Puzzle.domain.board.controller;

import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.service.BoardService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

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
    @PutMapping("/{id}")
    public void correctionPost(@PathVariable("id") Long id, @RequestBody CorrectionPostRequestDto request){
        boardService.correctionPost(id, request);
    }
}
