package com.server.Puzzle.domain.user.controller;

import com.server.Puzzle.domain.user.service.Impl.UserServiceImpl;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/token")
@RestController
public class TokenController {
    private final UserServiceImpl userService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    @GetMapping("/reissueToken")
    public ResponseEntity<Map<String, String>> reissueToken(HttpServletRequest request) {
        return ResponseEntity.ok().body(userService.reissueToken(jwtTokenProvider.resolveRefreshToken(request)));
    }
}
