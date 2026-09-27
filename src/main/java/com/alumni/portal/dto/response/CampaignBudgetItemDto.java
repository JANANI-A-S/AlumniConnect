package com.alumni.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignBudgetItemDto {
    private Long campaignId;
    private String campaignName;
    private BigDecimal targetCollections;
    private BigDecimal actualCollections;
    private BigDecimal campaignExpenses;
    private BigDecimal netContributions; // actualCollections - campaignExpenses
    private BigDecimal targetVariance; // actualCollections - targetCollections
    private double collectionPercentage;
    private String budgetStatus; // ON_TARGET, SURPLUS, DEFICIT
}
