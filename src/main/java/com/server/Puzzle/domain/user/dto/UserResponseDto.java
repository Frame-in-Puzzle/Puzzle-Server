package com.server.Puzzle.domain.user.dto;

import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

      private String name;
      private String email;
      private String imageUrl;
      private String bio;
      private Field field;
      private List<Language> language;
      private String url;

}