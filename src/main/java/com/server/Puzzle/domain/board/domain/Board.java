package com.server.Puzzle.domain.board.domain;

import com.server.Puzzle.domain.board.domain.enumType.Status;
import com.server.Puzzle.domain.board.domain.enumType.Tag;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "Board")
public class Board extends BaseTimeEntity {

    @Id @Column(name = "board_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Column(name = "board_content", nullable = false)
    private String content;

    @Column(name = "board_status", nullable = false)
    private Status status;

    @Column(name = "board_tag", nullable = false)
    private Tag tag;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
