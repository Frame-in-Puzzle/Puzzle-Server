package com.server.Puzzle.domain.user.domain;

import com.server.Puzzle.global.entity.BaseTimeEntity;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;

@Getter
@Entity
@Table(name = "User")
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = true)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_field", nullable = true)
    private Field field; //분야

    @Enumerated(STRING)
    @Column(name = "Role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Role", joinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    @Column(name = "user_bio", nullable = true)
    private String bio;

    @Column(name = "user_url", nullable = true)
    private String url;

    @Column(name = "user_image_url", nullable = false)
    private String imageUrl;

    @Builder
    public User(String email, String name, String imageUrl, Role roles, String bio) {
        this.email = email;
        this.name = name;
        this.roles = Collections.singletonList(roles);
        this.imageUrl = imageUrl;
        this.bio = bio;
    }

    public User update(String name, String email, String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> rolesConvertString = this.roles.stream().map(Enum::name).collect(Collectors.toList());
        return rolesConvertString.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
