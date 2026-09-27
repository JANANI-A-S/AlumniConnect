package com.alumni.portal.repository;

import com.alumni.portal.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    Optional<Campaign> findByName(String name);
    boolean existsByName(String name);
}
