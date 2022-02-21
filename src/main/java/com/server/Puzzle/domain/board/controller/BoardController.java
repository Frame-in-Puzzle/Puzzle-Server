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
import com.server.Puzzle.global.response.ResponseService;
import com.server.Puzzle.global.response.result.CommonResult;
import com.server.Puzzle.global.response.result.SingleResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/api/board")
@RestController
public class BoardController {

    private final BoardService boardService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @PostMapping("/")
    public CommonResult post(@RequestBody PostRequestDto request){
        boardService.post(request);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @PostMapping("/create-url")
    public SingleResult<String> createUrl(@RequestPart MultipartFile files) {
        return responseService.getSingleResult(boardService.createUrl(files));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @PutMapping("/{id}")
    public CommonResult correctionPost(@PathVariable("id") Long id, @RequestBody CorrectionPostRequestDto request){
        boardService.correctionPost(id, request);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @GetMapping
    public SingleResult<Page<GetAllPostResponseDto>> getAllPost(@PageableDefault(size = 12) Pageable pageable) {
        return responseService.getSingleResult(boardService.getAllPost(pageable));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @GetMapping("/{id}")
    public SingleResult<GetPostResponseDto> getPost(@PathVariable("id") Long id) {
        return responseService.getSingleResult(boardService.getPost(id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @DeleteMapping("/{id}")
    public CommonResult deletePost(@PathVariable("id") Long id){
        boardService.deletePost(id);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @GetMapping("/filter")
    public SingleResult<List<GetPostByTagResponseDto>> getPostByTag(@RequestParam Purpose purpose,
                                                      @RequestParam List<Field> field,
                                                      @RequestParam(defaultValue = "NULL") List<Language> language,
                                                      @RequestParam Status status,
                                                      @PageableDefault(size = 12) Pageable pageable)
    {
        return responseService.getSingleResult(boardService.getPostByTag(purpose, field, language, status, pageable));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        System.out.println(name + " parameter is missing");
        return name + " parameter is missing";
    }
}
