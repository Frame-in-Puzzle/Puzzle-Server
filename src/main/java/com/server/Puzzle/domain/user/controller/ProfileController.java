package com.server.Puzzle.domain.user.controller;

import com.server.Puzzle.domain.user.dto.UserBoardResponse;
import com.server.Puzzle.domain.user.dto.UserResponseDto;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
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
@RequestMapping("/api/profile")
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{githubId}")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable String githubId) {
        UserResponseDto profile = profileService.getProfile(githubId);
        return ResponseEntity.ok().body(profile);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/update")
    public ResponseEntity profileUpdate(@RequestBody UserUpdateDto userInfo) {
        profileService.profileUpdate(userInfo);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/{githubId}/board")
    public ResponseEntity<Page<UserBoardResponse>> getUserBoard(@PageableDefault(page = 10) Pageable pageable, @PathVariable String githubId) {
        return ResponseEntity.ok().body(profileService.getUserBoard(githubId, pageable));
    }
}
