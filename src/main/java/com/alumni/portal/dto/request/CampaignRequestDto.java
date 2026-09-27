package com.alumni.portal.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
public class CampaignRequestDto {

    @NotBlank(message = "Campaign name is required")
    private String name;

    private String description;

    @NotNull(message = "Target amount is required")
    @DecimalMin(value = "1.00", message = "Target amount must be at least 1.00")
    private BigDecimal targetAmount;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
