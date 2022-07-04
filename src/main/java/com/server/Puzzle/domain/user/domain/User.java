package com.server.Puzzle.domain.user.domain;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.global.entity.BaseTimeEntity;
import com.server.Puzzle.global.enumType.Field;
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
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Column(name = "oauth_idx")
    private String oauthIdx;

    @Column(name = "user_email", unique = false, nullable = true)
    private String email;

    @Column(name = "github_id", unique = true, nullable = false)
    private String githubId;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_field", nullable = true)
    private Field field; //분야

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<UserLanguage> userLanguages; // 세부언어

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default()
    private List<Roles> roles = new ArrayList<>();

    @Column(name = "user_bio", nullable = true)
    private String bio;

    @Column(name = "user_url", nullable = true)
    private String url;

    @Column(name = "user_profile_image_url", nullable = false)
    private String profileImageUrl;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "is_first_visit")
    private boolean isFirstVisited;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Board> boards;

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

    public User updateProfileImageUrl(String imageUrl){
        this.profileImageUrl = imageUrl != null ? imageUrl : this.profileImageUrl;
        return this;
    }

    public User updateField(Field field){
        this.field = field != null ? field : this.field;
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

    public User updateIsFirstVisited(boolean isFirstVisited){
        this.isFirstVisited = isFirstVisited;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> rolesConvertString = this.roles.stream().map(Roles::getAuthority).filter(Objects::nonNull).collect(Collectors.toList());
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
        return this.githubId;
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

