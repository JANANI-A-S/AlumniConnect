package com.alumni.portal.repository;

import com.alumni.portal.entity.MentorshipSession;
import com.alumni.portal.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorshipSessionRepository extends JpaRepository<MentorshipSession, Long> {
    List<MentorshipSession> findByMentorId(Long mentorId);
    List<MentorshipSession> findByStatus(SessionStatus status);
    List<MentorshipSession> findAllByOrderBySessionDateDesc();
    long countByStatus(SessionStatus status);
}
