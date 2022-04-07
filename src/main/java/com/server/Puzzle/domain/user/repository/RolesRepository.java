package com.server.Puzzle.domain.user.repository;

import com.server.Puzzle.domain.user.domain.Roles;
import com.server.Puzzle.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findRolesByUser(User user);

}
