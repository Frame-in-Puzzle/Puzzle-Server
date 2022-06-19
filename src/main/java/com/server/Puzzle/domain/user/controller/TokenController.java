package com.server.Puzzle.domain.user.controller;

import com.server.Puzzle.domain.user.service.TokenService;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/token")
@RestController
public class TokenController {

    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 만료된 토큰 재발급
     * @param githubId
     * @param request
     * @author 홍현인
     * @return Map<String, String> - AccessToken, RefreshToken
     */
    @ApiImplicitParams({@ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    @GetMapping("/reissue")
    public ResponseEntity<Map<String, String>> reissueToken(@RequestParam String githubId, HttpServletRequest request) {
        return ResponseEntity.ok().body(tokenService.reissueToken(jwtTokenProvider.resolveRefreshToken(request), githubId));
    }

}
