package com.server.Puzzle.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.Puzzle.domain.user.domain.QUser;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.dto.UserResponseDto;
import com.server.Puzzle.global.enumType.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.server.Puzzle.domain.user.domain.QUserLanguage.userLanguage;

@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserResponseDto findByUser(String githubId) {

        List<Language> languages = jpaQueryFactory.from(userLanguage)
                .select(userLanguage.language)
                .where(userLanguage.user.githubId.eq(githubId))
                .fetch();

        User user = jpaQueryFactory.from(QUser.user)
                .select(QUser.user)
                .where(QUser.user.githubId.eq(githubId))
                .fetchOne();

        UserResponseDto res = UserResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .field(user.getField())
                .bio(user.getBio())
                .language(languages)
                .url(user.getUrl())
                .build();

        return res;
    }
}
