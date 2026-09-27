package com.alumni.portal.repository;

import com.alumni.portal.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByCampaignId(Long campaignId);
    boolean existsByCampaignId(Long campaignId);
}
