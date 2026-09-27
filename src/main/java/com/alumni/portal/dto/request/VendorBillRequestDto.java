package com.alumni.portal.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorBillRequestDto {

    private Long purchaseOrderId;

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    private Long campaignId;

    @NotNull(message = "Bill Amount is required")
    @DecimalMin(value = "0.01", message = "Bill amount must be greater than zero")
    private BigDecimal amount;

    private LocalDate dueDate;

    private String notes;
}
