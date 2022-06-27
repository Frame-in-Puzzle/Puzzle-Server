package com.server.Puzzle.domain.user.dto;

import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProfileUpdateDto {

    private String name;
    private String email;
    private String bio;
    private Field field;
    private List<Language> language;

}
