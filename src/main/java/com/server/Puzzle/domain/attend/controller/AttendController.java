package com.server.Puzzle.domain.attend.controller;

import com.server.Puzzle.domain.attend.service.AttendService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/api/attend")
@RequiredArgsConstructor
@RestController
public class AttendController {

    private final AttendService attendService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseStatus( HttpStatus.OK )
    @PostMapping("/{boardId}")
    public ResponseEntity<String> requestAttend(@PathVariable Long boardId){
        attendService.requestAttend(boardId);
        return ResponseEntity.ok("Success");
    }
}
