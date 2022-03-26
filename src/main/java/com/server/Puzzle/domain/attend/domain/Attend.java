package com.server.Puzzle.domain.attend.domain;

import com.server.Puzzle.domain.attend.enumtype.AttendStatus;
import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "attend",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<AttendLanguage> languages;

    @Enumerated(EnumType.STRING)
    @Column(name = "attend_status")
    private AttendStatus attendStatus;

    public void updateAttendStatus(AttendStatus attendStatus){
        this.attendStatus = attendStatus;
    }

}
