package com.alumni.portal.repository;

import com.alumni.portal.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    Optional<Donation> findByReceiptNumber(String receiptNumber);

    List<Donation> findByAlumniIdOrderByDonationDateDesc(Long alumniId);

    List<Donation> findByCampaignIdOrderByDonationDateDesc(Long campaignId);

    List<Donation> findAllByOrderByDonationDateDesc();

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.campaign.id = :campaignId")
    BigDecimal sumDonationsByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d")
    BigDecimal sumTotalDonations();
}
