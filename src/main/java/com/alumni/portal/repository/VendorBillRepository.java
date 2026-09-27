package com.alumni.portal.repository;

import com.alumni.portal.entity.VendorBill;
import com.alumni.portal.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendorBillRepository extends JpaRepository<VendorBill, Long> {
    Optional<VendorBill> findByBillNumber(String billNumber);
    List<VendorBill> findByStatus(BillStatus status);
    List<VendorBill> findByVendorId(Long vendorId);
    List<VendorBill> findByCampaignId(Long campaignId);
    List<VendorBill> findByPurchaseOrderId(Long poId);

    @Query("SELECT COALESCE(SUM(vb.amount), 0) FROM VendorBill vb WHERE vb.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") BillStatus status);

    @Query("SELECT COALESCE(SUM(vb.amount), 0) FROM VendorBill vb WHERE vb.campaign.id = :campaignId")
    BigDecimal sumAmountByCampaignId(@Param("campaignId") Long campaignId);
}
