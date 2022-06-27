package com.server.Puzzle.domain.board.dto.request;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class PostRequestDto {

    private String title;
    private String contents;
    private Purpose purpose;
    private Status status;
    private String introduce;
    private List<Field> fields;
    private List<Language> languages;
    private List<String> fileUrls;

    public Board dtoToEntity(User user){
        return Board.builder()
                .title(this.getTitle())
                .contents(this.getContents())
                .purpose(this.getPurpose())
                .status(this.getStatus())
                .user(user)
                .introduce(this.getIntroduce())
                .build();
    }
}

