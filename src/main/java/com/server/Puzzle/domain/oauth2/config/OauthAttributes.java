package com.server.Puzzle.domain.oauth2.config;

import com.server.Puzzle.domain.oauth2.dto.UserProfile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OauthAttributes {

    public UserProfile extract(Map<String, Object> attributes) {
        return UserProfile.builder()
                .oauthIdx(String.valueOf(attributes.get("id")))
                .email((String) attributes.get("email"))
                .githubId((String) attributes.get("login"))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("avatar_url"))
                .bio((String) attributes.get("bio"))
                .build();
    }

}
