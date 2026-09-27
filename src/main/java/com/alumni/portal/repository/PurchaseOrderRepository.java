package com.alumni.portal.repository;

import com.alumni.portal.entity.PurchaseOrder;
import com.alumni.portal.enums.POStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByPoNumber(String poNumber);
    List<PurchaseOrder> findByCampaignId(Long campaignId);
    List<PurchaseOrder> findByVendorId(Long vendorId);
    List<PurchaseOrder> findByStatus(POStatus status);

    @Query("SELECT COALESCE(SUM(po.amount), 0) FROM PurchaseOrder po WHERE po.campaign.id = :campaignId")
    BigDecimal sumAmountByCampaignId(@Param("campaignId") Long campaignId);
}
