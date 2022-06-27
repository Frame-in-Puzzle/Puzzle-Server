package com.server.Puzzle.domain.board.domain;

import com.server.Puzzle.domain.attend.domain.Attend;
import com.server.Puzzle.domain.attend.enumtype.AttendStatus;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder @Getter
@Entity @Table(name = "Board")
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Column(name = "board_contents", nullable = false, length = 10000)
    private String contents;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_purpose", nullable = false)
    private Purpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_status", nullable = false)
    private Status status;

    @Column(name = "board_introduce", nullable = false)
    private String introduce;

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<BoardField> boardFields;

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<BoardLanguage> boardLanguages;

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<BoardFile> boardFiles;

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Attend> attends;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Board updateTitle(String title){
        this.title = title != null ? title : this.title;
        return this;
    }

    public Board updateContents(String contents) {
        this.contents = contents != null ? contents : this.contents;
        return this;
    }

    public void updateIntroduce(String introduce){
        this.introduce = introduce != null ? introduce : this.introduce;
    }

    public Board updatePurpose(Purpose purpose) {
        this.purpose = purpose;
        return this;
    }

    public Board updateStatus(Status status) {
        this.status = status;
        return this;
    }

    public boolean isAttended(User currentUser){
        return this.getAttends().stream()
                .anyMatch(b -> b.getUser().equals(currentUser));
    }

    public void updateAttendStatus(Long attendId, AttendStatus attendStatus){
        this.getAttends().stream()
                .filter(a ->
                        a.getId().equals(attendId)
                )
                .findFirst()
                .orElseThrow()
                .updateAttendStatus(attendStatus);
    }

    public boolean isAuthor(User currentUser){
        return this.getUser().equals(currentUser);
    }
}