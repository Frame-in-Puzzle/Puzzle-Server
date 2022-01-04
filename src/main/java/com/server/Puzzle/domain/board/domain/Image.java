package com.server.Puzzle.domain.board.domain;

import com.server.Puzzle.global.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity @Table(name = "Image")
public class Image extends BaseTimeEntity {

    @Column(name = "image_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_name", nullable = false)
    private String name;

    @Column(name= "image_path", nullable = false)
    private String path;

    @JoinColumn(name = "board_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
