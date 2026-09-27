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
public class BalanceSheetReportDto {
    private LocalDate asOfDate;
    private List<AccountBalanceDto> assetAccounts;
    private BigDecimal totalAssets;
    private List<AccountBalanceDto> liabilityAccounts;
    private BigDecimal totalLiabilities;
    private BigDecimal netEndowmentEquity; // Assets - Liabilities
    private BigDecimal totalLiabilitiesAndEquity; // Liabilities + Equity (equals Assets)
    private boolean isBalanced;
}
