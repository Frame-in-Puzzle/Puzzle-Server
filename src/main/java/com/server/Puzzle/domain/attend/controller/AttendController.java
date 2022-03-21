package com.server.Puzzle.domain.attend.controller;

import com.server.Puzzle.domain.attend.dto.request.PatchAttendRequest;
import com.server.Puzzle.domain.attend.dto.response.GetAllAttendResponse;
import com.server.Puzzle.domain.attend.service.AttendService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/attend")
@RequiredArgsConstructor
@RestController
public class AttendController {

    private final AttendService attendService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/board/{boardId}")
    public ResponseEntity<String> requestAttend(@PathVariable Long boardId){
        attendService.requestAttend(boardId);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<GetAllAttendResponse>> getAllAttend(@PathVariable Long boardId){
        List<GetAllAttendResponse> response = attendService.findAllAttend(boardId);
        return ResponseEntity.ok().body(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping("/{attendId}")
    public ResponseEntity<String> patchAttend(@PathVariable Long attendId, @RequestBody PatchAttendRequest patchAttendRequest){
        attendService.patchAttend(attendId, patchAttendRequest);
        return ResponseEntity.ok("Success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/{attendId}")
    public ResponseEntity<String> deleteAttend(@PathVariable Long attendId) {
        attendService.deleteAttend(attendId);
        return ResponseEntity.ok("Success");
    }
}
