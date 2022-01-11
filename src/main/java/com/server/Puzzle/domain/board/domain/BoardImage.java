package com.server.Puzzle.domain.board.domain;

import com.server.Puzzle.global.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity @Table(name = "Board_Image")
public class BoardImage extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "image_url", nullable = false)
    private String url;
}