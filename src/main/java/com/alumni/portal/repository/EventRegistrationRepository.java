package com.alumni.portal.repository;

import com.alumni.portal.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByEventId(Long eventId);
    List<EventRegistration> findByAlumniId(Long alumniId);
    boolean existsByEventIdAndAlumniId(Long eventId, Long alumniId);
    long countByEventId(Long eventId);
}
