package com.server.Puzzle.domain.user.oauth2.service;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.enumType.OauthAttributes;
import com.server.Puzzle.domain.user.oauth2.OauthProperties;
import com.server.Puzzle.domain.user.oauth2.dto.LoginResponse;
import com.server.Puzzle.domain.user.oauth2.dto.OauthTokenResponse;
import com.server.Puzzle.domain.user.oauth2.dto.UserProfile;
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
public class OauthService {
    private final UserRepository userRepository;
    private final OauthProperties oauthProperties;
    private final JwtTokenProvider jwtTokenProvider;
    public LoginResponse login(String providerName, String code) {
        // access token 가져오기
        OauthTokenResponse tokenResponse = getToken(code);
        // 유저 정보 가져오기
        UserProfile userProfile = getUserProfile(providerName, tokenResponse);
        // 유저 DB에 저장
        User user = saveOrUpdate(userProfile);

        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getName()), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return LoginResponse.builder()
                .id(user.getId())
                .oauthId(user.getOauthId())
                .name(user.getName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .roles(user.getRoles())
                .bio(user.getBio())
                .build();
    }

    private User saveOrUpdate(UserProfile userProfile) {
        User user = userRepository.findByOauthId(userProfile.getOauthId())
                .map(entity -> entity.update(
                        userProfile.getEmail(), userProfile.getName()))
                .orElseGet(userProfile::toUser);
        return userRepository.save(user);
    }

    private UserProfile getUserProfile(String providerName, OauthTokenResponse tokenResponse) {
        Map<String, Object> userAttributes = getUserAttributes(tokenResponse);
        return OauthAttributes.extract(providerName, userAttributes);
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

    private OauthTokenResponse getToken(String code) {
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

    private MultiValueMap<String, String> tokenRequest(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", oauthProperties.getRedirectUri());
        return formData;
    }
}
