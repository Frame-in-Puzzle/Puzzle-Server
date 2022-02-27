package com.server.Puzzle.domain.attend.repository;

import com.server.Puzzle.domain.attend.domain.Attend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendRepository extends JpaRepository<Attend,Long>,AttendCustomRepository {

}
