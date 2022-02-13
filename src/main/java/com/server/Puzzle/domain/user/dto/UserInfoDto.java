package com.server.Puzzle.domain.user.dto;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
     private String name;
     private String email;
     private String imageUrl;
     private String bio;
     private Field field;
     private Language language;
     private String url;

     @Builder
     public UserInfoDto(User user) {
          this.name = user.getName();
          this.imageUrl = user.getImageUrl();
          this.bio = user.getBio();
          this.field = user.getField();
          this.language = user.getLanguage();
          this.url = user.getUrl();
          this.email = user.getEmail();
     }
}