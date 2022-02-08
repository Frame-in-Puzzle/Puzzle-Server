package com.server.Puzzle.domain.user.controller;

import com.server.Puzzle.domain.user.dto.UserInfoDto;
import com.server.Puzzle.domain.user.service.ProfileService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/{name}")
    public ResponseEntity<UserInfoDto> getProfile(@PathVariable String name) {
        UserInfoDto userInfo = profileService.getProfile(name);
        return ResponseEntity.ok().body(userInfo);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/update")
    public void profileUpdate(@RequestBody UserInfoDto userInfo) {
        profileService.profileUpdate(userInfo);
    }
}
