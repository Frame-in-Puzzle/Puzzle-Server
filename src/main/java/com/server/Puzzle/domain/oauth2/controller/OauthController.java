package com.server.Puzzle.domain.oauth2.controller;

import com.server.Puzzle.domain.oauth2.service.impl.OauthServiceImpl;
import com.server.Puzzle.domain.oauth2.dto.LoginResponse;
import com.server.Puzzle.domain.oauth2.dto.OauthCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/oauth")
@RestController()
public class OauthController {

    private final OauthServiceImpl oauthServiceImpl;

    /**
     * Github Oauth 로그인
     * @param code code
     * @return githubId, email, isFirstVisited, accessToken, refreshToken
     * @author 홍현인
     */
    @PostMapping("/login/github")
    public ResponseEntity<LoginResponse> login(@RequestBody OauthCode code)  {
        LoginResponse loginResponse = oauthServiceImpl.login(code);
        return ResponseEntity.ok(loginResponse);
    }

}
