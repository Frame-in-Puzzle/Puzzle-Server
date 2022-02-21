package com.server.Puzzle.domain.oauth2.controller;

import com.server.Puzzle.domain.oauth2.service.impl.OauthServiceImpl;
import com.server.Puzzle.domain.oauth2.dto.LoginResponse;
import com.server.Puzzle.domain.oauth2.dto.OauthCode;
import com.server.Puzzle.global.response.ResponseService;
import com.server.Puzzle.global.response.result.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/api/oauth")
@RestController()
public class OauthController {
    private final OauthServiceImpl oauthServiceImpl;
    private final ResponseService responseService;

    @PostMapping("/login/github")
    public SingleResult<LoginResponse> login(@RequestBody OauthCode code)  {
        LoginResponse loginResponse = oauthServiceImpl.login(code);
        return responseService.getSingleResult(loginResponse);
    }
}
