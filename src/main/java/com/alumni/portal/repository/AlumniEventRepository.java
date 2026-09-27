package com.alumni.portal.repository;

import com.alumni.portal.entity.AlumniEvent;
import com.alumni.portal.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumniEventRepository extends JpaRepository<AlumniEvent, Long> {
    List<AlumniEvent> findByStatus(EventStatus status);
    List<AlumniEvent> findAllByOrderByEventDateAsc();
    List<AlumniEvent> findAllByOrderByEventDateDesc();
    long countByStatus(EventStatus status);
}
