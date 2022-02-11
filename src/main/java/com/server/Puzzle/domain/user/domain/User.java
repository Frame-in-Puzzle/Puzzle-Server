package com.server.Puzzle.domain.user.domain;

import com.server.Puzzle.global.entity.BaseTimeEntity;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.enumType.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User")
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column(name = "user_email", unique = true, nullable = true)
    private String email;

    @Column(name = "github_id", unique = true, nullable = false)
    private String githubId;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_field", nullable = true)
    private Field field; //분야

    @Column(name = "language", nullable = true)
    private Language language; // 세부언어

    @Enumerated(EnumType.STRING)
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

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "is_first_visit")
    private boolean isFirstVisit;

    public User updateGithubId(String githubId) {
        this.githubId = githubId != null ? githubId : this.githubId;
        return this;
    }
    public User updateName(String name){
        this.name = name != null ? name : this.name;
        return this;
    }

    public User updateEmail(String email){
        this.email = email != null ? email : this.email;
        return this;
    }

    public User updateImageUrl(String imageUrl){
        this.imageUrl = imageUrl != null ? imageUrl : this.imageUrl;
        return this;
    }

    public User updateField(Field field){
        this.field = field != null ? field : this.field;
        return this;
    }

    public User updateLanguage(Language language){
        this.language = language != null ? language : this.language;
        return this;
    }

    public User updateBio(String bio){
        this.bio = bio != null ? bio : this.bio;
        return this;
    }

    public User updateUrl(String url){
        this.url = url != null ? url : this.url;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> rolesConvertString = this.roles.stream().map(Enum::name).collect(Collectors.toList());
        return rolesConvertString.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

