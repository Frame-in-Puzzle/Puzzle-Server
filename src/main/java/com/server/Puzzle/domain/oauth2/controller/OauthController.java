package com.server.Puzzle.domain.oauth2.controller;

import com.server.Puzzle.domain.oauth2.service.impl.OauthServiceImpl;
import com.server.Puzzle.domain.oauth2.dto.LoginResponse;
import com.server.Puzzle.domain.oauth2.dto.OauthCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/api/oauth")
@RestController()
public class OauthController {
    private final OauthServiceImpl oauthServiceImpl;

    @GetMapping("/login/github")
    public ResponseEntity<LoginResponse> login(@RequestBody OauthCode code) {
        LoginResponse loginResponse = oauthServiceImpl.login(code);
        return ResponseEntity.ok().body(loginResponse);
    }
}
