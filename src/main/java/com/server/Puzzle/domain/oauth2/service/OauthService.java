package com.server.Puzzle.domain.oauth2.service;

import com.server.Puzzle.domain.oauth2.dto.LoginResponse;
import com.server.Puzzle.domain.oauth2.dto.OauthCode;
public interface OauthService {

    public LoginResponse login(OauthCode code);

}
