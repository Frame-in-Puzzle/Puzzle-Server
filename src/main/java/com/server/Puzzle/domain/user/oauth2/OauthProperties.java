package com.server.Puzzle.domain.user.oauth2;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OauthProperties {
        @Value("${oauth2.user.github.client-id}")
        private String clientId;
        @Value("${oauth2.user.github.client-secret}")
        private String clientSecret;
        @Value("${oauth2.user.github.redirect-uri}")
        private String redirectUri;
        @Value("${oauth2.provider.github.token-uri}")
        private String tokenUri;
        @Value("${oauth2.provider.github.user-info-uri}")
        private String userInfoUri;
}