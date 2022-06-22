package com.server.Puzzle.domain.attend.controller;

import com.server.Puzzle.domain.attend.dto.request.PatchAttendRequest;
import com.server.Puzzle.domain.attend.dto.response.FindAllAttendResponse;
import com.server.Puzzle.domain.attend.service.AttendService;
import com.server.Puzzle.domain.board.enumType.IsAttendStatus;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AttendController <br>
 * 프로젝트, 대회, 서비스 등의 참가 Controller
 */
@RequestMapping("/api/attend")
@RequiredArgsConstructor
@RestController
public class AttendController {

    private final AttendService attendService;

    /**
     * 참가요청
     * @param boardId
     * @return ResponseEntity - Success
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/board/{boardId}")
    public ResponseEntity<String> requestAttend(@PathVariable Long boardId){
        attendService.requestAttend(boardId);
        return ResponseEntity.ok("Success");
    }

    /**
     * 참가요청목록 전체조회
     * @param boardId
     * @return List FindAllAttendResponse - id, languages, name, githubId, imageUrl, attendStatus
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<FindAllAttendResponse>> FindAllAttend(@PathVariable Long boardId){
        List<FindAllAttendResponse> response = attendService.findAllAttend(boardId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 참가요청 상태 수정
     * @param attendId
     * @param patchAttendRequest attendStatus
     * @return ResponseEntity - Success
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping("/{attendId}")
    public ResponseEntity<String> patchAttend(@PathVariable Long attendId, @RequestBody PatchAttendRequest patchAttendRequest){
        attendService.patchAttend(attendId, patchAttendRequest);
        return ResponseEntity.ok("Success");
    }

    /**
     * 참가요청 취소
     * @param boardId
     * @return ResponseEntity - Success
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<String> deleteAttend(@PathVariable Long boardId) {
        attendService.deleteAttend(boardId);
        return ResponseEntity.ok("Success");
    }

    /**
     * 참가요청 상태확인
     * @param boardId
     * @return IsAttendStatus(Enum)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/status/board/{boardId}")
    public ResponseEntity<IsAttendStatus> checkAttendStatus(@PathVariable Long boardId) {
        return ResponseEntity.ok().body(attendService.checkAttendStatus(boardId));
    }

}
