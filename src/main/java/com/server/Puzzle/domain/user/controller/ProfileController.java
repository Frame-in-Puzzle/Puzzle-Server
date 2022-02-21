package com.server.Puzzle.domain.user.controller;

import com.server.Puzzle.domain.user.dto.MyBoardResponse;
import com.server.Puzzle.domain.user.dto.UserInfoDto;
import com.server.Puzzle.domain.user.service.ProfileService;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.exception.ErrorCode;
import com.server.Puzzle.global.response.ResponseService;
import com.server.Puzzle.global.response.result.CommonResult;
import com.server.Puzzle.global.response.result.SingleResult;
import com.server.Puzzle.global.util.CurrentUserUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.server.Puzzle.global.exception.ErrorCode.UNAUTHORIZED_USER;

@RequiredArgsConstructor
@RequestMapping("/v1/api/profile")
@RestController
public class ProfileController {

    private final CurrentUserUtil currentUserUtil;
    private final ProfileService profileService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{githubId}")
    public SingleResult<UserInfoDto> getProfile(@PathVariable String githubId) {
        UserInfoDto userInfo = profileService.getProfile(githubId);
        return responseService.getSingleResult(userInfo);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/update")
    public CommonResult profileUpdate(@RequestBody UserInfoDto userInfo) {
        profileService.profileUpdate(userInfo);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 토큰", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/board/{githubId}")
    public SingleResult<Page<MyBoardResponse>> getMyBoard(@PageableDefault(page = 10) Pageable pageable, @PathVariable String githubId) {
        if(!Objects.equals(currentUserUtil.getCurrentUser().getGithubId(), githubId))
            throw new CustomException(UNAUTHORIZED_USER);
        return responseService.getSingleResult(profileService.getMyBoard(githubId, pageable));
    }
}
