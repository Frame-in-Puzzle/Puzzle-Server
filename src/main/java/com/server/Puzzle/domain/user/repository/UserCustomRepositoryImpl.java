package com.server.Puzzle.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.dto.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

import static com.server.Puzzle.domain.user.domain.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserProfileResponse findByUser(String githubId) {
        User user1  = jpaQueryFactory
                .selectFrom(user)
                .where(user.githubId.eq(githubId))
                .fetchOne();

        return UserProfileResponse.builder()
                .name(user1.getName())
                .email(user1.getEmail())
                .imageUrl(user1.getImageUrl())
                .bio(user1.getBio())
                .field(user1.getField())
                .url(user1.getUrl())
                .languages(user1.getUserLanguages().stream()
                        .map(UserLanguage::getLanguage)
                        .collect(Collectors.toList()))
                .build();
    }

}
