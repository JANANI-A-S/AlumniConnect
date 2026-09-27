package com.alumni.portal.dto.response;

import com.alumni.portal.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationReceiptDto {
    private String receiptNumber;
    private Long donationId;
    private Long alumniId;
    private String alumniName;
    private String alumniEmail;
    private Long campaignId;
    private String campaignName;
    private String productName;
    private BigDecimal amount;
    private LocalDateTime donationDate;
    private PaymentMethod paymentMethod;
    private String notes;
    private Long journalEntryId;
    private String debitAccountName;
    private String creditAccountName;
    private String accountingStatus;
}
