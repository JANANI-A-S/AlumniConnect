package com.alumni.portal.repository;

import com.alumni.portal.entity.Alumni;
import com.alumni.portal.enums.RankingTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, Long> {

    Optional<Alumni> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Alumni> findAllByOrderByDonationTotalDesc();

    List<Alumni> findByBatchOrderByDonationTotalDesc(String batch);

    List<Alumni> findByDepartmentOrderByDonationTotalDesc(String department);

    List<Alumni> findByRankingTierOrderByDonationTotalDesc(RankingTier rankingTier);

    @Query("SELECT a FROM Alumni a WHERE " +
           "(:query IS NULL OR :query = '' OR " +
           " LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(a.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(a.batch) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(COALESCE(a.department, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(COALESCE(a.currentOrganization, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(COALESCE(a.cityLocation, '')) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:batch IS NULL OR :batch = '' OR a.batch = :batch) " +
           "AND (:department IS NULL OR :department = '' OR a.department = :department) " +
           "AND (:tier IS NULL OR a.rankingTier = :tier) " +
           "ORDER BY a.donationTotal DESC")
    List<Alumni> filterAndRankAlumni(
            @Param("query") String query,
            @Param("batch") String batch,
            @Param("department") String department,
            @Param("tier") RankingTier tier
    );

    @Query("SELECT a FROM Alumni a WHERE " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.batch) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(COALESCE(a.department, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(COALESCE(a.currentOrganization, '')) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY a.donationTotal DESC")
    List<Alumni> searchAlumni(@Param("query") String query);
}
