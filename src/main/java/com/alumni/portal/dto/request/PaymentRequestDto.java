package com.alumni.portal.dto.request;

import com.alumni.portal.enums.PaymentMethod;
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
public class PaymentRequestDto {

    @NotNull(message = "Vendor Bill ID is required")
    private Long vendorBillId;

    @NotNull(message = "Payment Amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
    private BigDecimal amount;

    @Builder.Default
    private PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;

    private String reference;
}
