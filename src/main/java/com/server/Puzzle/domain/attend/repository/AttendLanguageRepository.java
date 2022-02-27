package com.server.Puzzle.domain.attend.repository;

import com.server.Puzzle.domain.attend.domain.AttendLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendLanguageRepository extends JpaRepository<AttendLanguage,Long> {

}
