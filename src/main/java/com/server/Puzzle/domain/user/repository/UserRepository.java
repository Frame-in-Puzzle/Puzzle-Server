package com.server.Puzzle.domain.user.repository;

import com.server.Puzzle.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

    Optional<User> findByName(String username);
    Optional<User> findByGithubId(String githubId);

}
