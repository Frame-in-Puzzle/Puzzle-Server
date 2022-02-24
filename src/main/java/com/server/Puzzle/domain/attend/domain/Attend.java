package com.server.Puzzle.domain.attend.domain;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder @Getter
@Entity @Table(name = "Attend")
public class Attend extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private String githubId;

}
