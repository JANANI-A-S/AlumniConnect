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
public class ProfitLossReportDto {
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<AccountBalanceDto> revenueAccounts;
    private BigDecimal totalRevenue;
    private List<AccountBalanceDto> expenseAccounts;
    private BigDecimal totalExpenses;
    private BigDecimal netSurplus; // Revenue - Expenses
    private double operatingMarginPercentage;
}
