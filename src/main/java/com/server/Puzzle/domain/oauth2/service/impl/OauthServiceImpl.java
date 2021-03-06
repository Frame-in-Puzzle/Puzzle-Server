package com.server.Puzzle.domain.oauth2.service.impl;

import com.server.Puzzle.domain.oauth2.config.OauthAttributes;
import com.server.Puzzle.domain.oauth2.config.OauthProperties;
import com.server.Puzzle.domain.oauth2.dto.LoginResponse;
import com.server.Puzzle.domain.oauth2.dto.OauthCode;
import com.server.Puzzle.domain.oauth2.dto.OauthTokenResponse;
import com.server.Puzzle.domain.oauth2.dto.UserProfile;
import com.server.Puzzle.domain.oauth2.service.OauthService;
import com.server.Puzzle.domain.user.domain.Roles;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.dto.RolesProfile;
import com.server.Puzzle.domain.user.repository.RolesRepository;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.global.enumType.Role;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OauthServiceImpl implements OauthService {

    private final UserRepository userRepository;
    private final OauthProperties oauthProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final OauthAttributes oauthAttributes;
    private final RolesRepository rolesRepository;

    @Transactional
    @Override
    public LoginResponse login(OauthCode code) {
        // access token 가져오기
        OauthTokenResponse tokenResponse = getToken(code);
        // 유저 정보 가져오기
        UserProfile userProfile = getUserProfile(tokenResponse);
        // 유저 DB에 저장
        User user = save(userProfile);

        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getGithubId()), saveOrGetRoles(user));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);

        return LoginResponse.builder()
                .githubId(user.getGithubId())
                .email(user.getEmail())
                .isFirstVisited(user.isFirstVisited())
                .accessToken("Bearer " + accessToken)
                .refreshToken("Bearer " + refreshToken)
                .build();
    }

    private List<Roles> saveOrGetRoles(User user) {
        if(user.getRoles() == null) {
            RolesProfile roles = RolesProfile.builder()
                    .role(Role.ROLE_USER)
                    .user(user)
                    .build();

            Roles rolesByUser = rolesRepository.findRolesByUser(user)
                    .orElseGet(roles::toRoles);

            return List.of(rolesRepository.save(rolesByUser));
        } else {
            return user.getRoles();
        }
    }

    private User save(UserProfile userProfile) {
        User user = userRepository.findByGithubId(userProfile.getGithubId())
                .orElseGet(userProfile::toUser);

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
