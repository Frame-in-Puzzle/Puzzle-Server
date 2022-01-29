package com.server.Puzzle.domain.user.enumType;

import com.server.Puzzle.domain.user.oauth2.dto.UserProfile;

import java.util.Arrays;
import java.util.Map;

public enum OauthAttributes {
    GITHUB("github") {

        public UserProfile of(Map<String, Object> attributes) {
            System.out.println(attributes);
            return UserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("id")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("avatar_url"))
                    .bio((String) attributes.get("bio"))
                    .build();
        }
    };

    private final String providerName;

    OauthAttributes(String name) {
        this.providerName = name;
    }

    public static UserProfile extract(String providerName, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(attributes);
    }

    public abstract UserProfile of(Map<String, Object> attributes);
}
