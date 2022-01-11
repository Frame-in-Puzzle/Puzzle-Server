package com.server.Puzzle.domain.board.domain;

import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity @Table(name = "Board")
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Column(name = "board_contents", nullable = false)
    private String contents;

    @Column(name = "board_purpose", nullable = false)
    private Purpose purpose;

    @Column(name = "board_status", nullable = false)
    private Status status;

}