package com.server.Puzzle.domain.user.dto;

import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class UserUpdateDto {

    private String name;
    private String email;
    private String profileImageUrl;
    private String bio;
    private Field field;
    private List<Language> languages;

}
