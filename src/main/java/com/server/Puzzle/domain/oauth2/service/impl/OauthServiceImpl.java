package com.server.Puzzle.domain.oauth2.service.impl;

import com.server.Puzzle.domain.oauth2.service.OauthService;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.oauth2.config.OauthAttributes;
import com.server.Puzzle.domain.oauth2.config.OauthProperties;
import com.server.Puzzle.domain.oauth2.dto.LoginResponse;
import com.server.Puzzle.domain.oauth2.dto.OauthCode;
import com.server.Puzzle.domain.oauth2.dto.OauthTokenResponse;
import com.server.Puzzle.domain.oauth2.dto.UserProfile;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OauthServiceImpl implements OauthService {
    private final UserRepository userRepository;
    private final OauthProperties oauthProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final OauthAttributes oauthAttributes;

    @Override
    public LoginResponse login(OauthCode code) {
        // access token 가져오기
        OauthTokenResponse tokenResponse = getToken(code);
        // 유저 정보 가져오기
        UserProfile userProfile = getUserProfile(tokenResponse);
        // 유저 DB에 저장
        User user = saveOrUpdate(userProfile);

        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getName()), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return LoginResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private User saveOrUpdate(UserProfile userProfile) {
        User user = userRepository.findByOauthId(userProfile.getOauthId())
                .map(entity -> entity.update(
                        userProfile.getEmail(), userProfile.getName()))
                .orElseGet(userProfile::toUser);
        System.out.println(user);
        return userRepository.save(user);
    }

    private UserProfile getUserProfile(OauthTokenResponse tokenResponse) {
        Map<String, Object> userAttributes = getUserAttributes(tokenResponse);
        return oauthAttributes.extract(userAttributes);
    }

    private Map<String, Object> getUserAttributes(OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(oauthProperties.getUserInfoUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    private OauthTokenResponse getToken(OauthCode code) {
        return WebClient.create()
                .post()
                .uri(oauthProperties.getTokenUri())
                .headers(header -> {
                    header.setBasicAuth(oauthProperties.getClientId(), oauthProperties.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code))
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(OauthCode code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code.getCode());
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", oauthProperties.getRedirectUri());
        return formData;
    }
}
