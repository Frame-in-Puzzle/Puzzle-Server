package com.server.Puzzle.domain.user.service.Impl;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.TokenService;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static com.server.Puzzle.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public Map<String, String> reissueToken(String refreshToken, String githubId) {
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (user.getRefreshToken() == null) {
            throw new CustomException(NOT_LOGGED_IN);
        }

        if(!(user.getRefreshToken().equals(refreshToken) && jwtTokenProvider.validateToken(refreshToken))) {
            throw new CustomException(UNABLE_TO_ISSUANCE_REFRESHTOKEN);
        }

        Map<String, String> map = new HashMap<>();

        String newAccessToken = jwtTokenProvider.createToken(user.getGithubId(), user.getRoles());
        String newRefreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(newRefreshToken);

        map.put("RefreshToken", "Bearer " + newRefreshToken);
        map.put("AccessToken", "Bearer " + newAccessToken);

        return map;
    }

}
