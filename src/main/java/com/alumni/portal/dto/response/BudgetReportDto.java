package com.alumni.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetReportDto {
    private LocalDate generatedDate;
    private List<CampaignBudgetItemDto> campaignBudgets;
    private BigDecimal totalTargetCollections;
    private BigDecimal totalActualCollections;
    private BigDecimal totalCampaignExpenses;
    private BigDecimal totalNetContributions;
    private BigDecimal totalVariance;
}
