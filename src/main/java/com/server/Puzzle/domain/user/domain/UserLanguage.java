package com.server.Puzzle.domain.user.domain;

import com.server.Puzzle.global.entity.BaseTimeEntity;
import com.server.Puzzle.global.enumType.Language;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity @Table(name = "User_Language")
public class UserLanguage extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lang_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_language", nullable = false)
    private Language language;
}
