package com.alumni.portal.dto.response;

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
public class CampaignResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal targetAmount;
    private BigDecimal collectedAmount;
    private BigDecimal remainingAmount;
    private double percentageAchieved;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
}
