package com.server.Puzzle.domain.user.domain;

import com.server.Puzzle.global.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity @Table(name = "User")
public class User extends BaseTimeEntity {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(name = "user_image_url", nullable = false)
    private String image;

}
