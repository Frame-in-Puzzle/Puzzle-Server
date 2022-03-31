package com.server.Puzzle.domain.user.dto;

import com.server.Puzzle.domain.user.domain.Roles;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.enumType.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class RolesProfile {

    private User user;
    private Role role;

    public Roles toRoles() {
        return Roles.builder()
                .role(role)
                .user(user)
                .build();
    }

}
