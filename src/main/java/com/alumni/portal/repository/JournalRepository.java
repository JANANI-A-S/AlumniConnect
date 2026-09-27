package com.alumni.portal.repository;

import com.alumni.portal.entity.Journal;
import com.alumni.portal.enums.JournalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    Optional<Journal> findByType(JournalType type);
    Optional<Journal> findByCode(String code);
    List<Journal> findAllByType(JournalType type);
}
