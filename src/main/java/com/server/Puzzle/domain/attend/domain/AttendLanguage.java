package com.server.Puzzle.domain.attend.domain;

import com.server.Puzzle.global.entity.BaseTimeEntity;
import com.server.Puzzle.global.enumType.Language;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder @Getter
@Entity @Table(name = "AttendLanguage")
public class AttendLanguage extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attend_language_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attend_id")
    private Attend attend;

    @Enumerated(EnumType.STRING)
    @Column(name = "attend_language_language")
    private Language language;

}
