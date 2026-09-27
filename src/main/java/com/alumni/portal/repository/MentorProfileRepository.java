package com.alumni.portal.repository;

import com.alumni.portal.entity.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {
    Optional<MentorProfile> findByAlumniId(Long alumniId);
    List<MentorProfile> findByActiveTrue();
    long countByActiveTrue();
}
