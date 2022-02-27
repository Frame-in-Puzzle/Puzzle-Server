package com.server.Puzzle.domain.user.repository;

import com.server.Puzzle.domain.user.domain.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
    void deleteAllByUserId(Long id);
}
