package com.server.Puzzle.domain.user.controller;

import com.server.Puzzle.domain.user.dto.MyBoardResponse;
import com.server.Puzzle.domain.user.dto.UserInfoDto;
import com.server.Puzzle.domain.user.service.ProfileService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/api/profile")
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{githubId}")
    public ResponseEntity<UserInfoDto> getProfile(@PathVariable String githubId) {
        UserInfoDto userInfo = profileService.getProfile(githubId);
        return ResponseEntity.ok().body(userInfo);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/update")
    public void profileUpdate(@RequestBody UserInfoDto userInfo) {
        profileService.profileUpdate(userInfo);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/board/{githubId}")
    public Page<MyBoardResponse> getMyBoard(@PageableDefault(page = 10) Pageable pageable, @PathVariable String githubId) {
        return profileService.getMyBoard(githubId, pageable);
    }
}
