package com.alumni.portal.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderRequestDto {

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    @NotNull(message = "Campaign ID is required")
    private Long campaignId;

    @NotNull(message = "PO Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    private String description;
}
