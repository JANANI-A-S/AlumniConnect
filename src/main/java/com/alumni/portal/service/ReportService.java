package com.alumni.portal.service;

import com.alumni.portal.dto.response.*;
import com.alumni.portal.entity.Campaign;
import com.alumni.portal.entity.ChartOfAccounts;
import com.alumni.portal.enums.AccountType;
import com.alumni.portal.repository.CampaignRepository;
import com.alumni.portal.repository.ChartOfAccountsRepository;
import com.alumni.portal.repository.DonationRepository;
import com.alumni.portal.repository.VendorBillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ChartOfAccountsRepository accountRepository;
    private final CampaignRepository campaignRepository;
    private final DonationRepository donationRepository;
    private final VendorBillRepository billRepository;

    /**
     * 1. Balance Sheet: Endowment fund bank balances and investments vs. pending campaign payables.
     */
    @Transactional(readOnly = true)
    public BalanceSheetReportDto getBalanceSheet() {
        List<ChartOfAccounts> assets = accountRepository.findByType(AccountType.ASSET);
        List<ChartOfAccounts> liabilities = accountRepository.findByType(AccountType.LIABILITY);

        List<AccountBalanceDto> assetDtos = assets.stream()
                .map(this::toAccountBalanceDto)
                .collect(Collectors.toList());

        List<AccountBalanceDto> liabilityDtos = liabilities.stream()
                .map(this::toAccountBalanceDto)
                .collect(Collectors.toList());

        BigDecimal totalAssets = assets.stream()
                .map(ChartOfAccounts::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalLiabilities = liabilities.stream()
                .map(ChartOfAccounts::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netEndowmentEquity = totalAssets.subtract(totalLiabilities);
        BigDecimal totalLiabilitiesAndEquity = totalLiabilities.add(netEndowmentEquity);

        boolean isBalanced = totalAssets.compareTo(totalLiabilitiesAndEquity) == 0;

        return BalanceSheetReportDto.builder()
                .asOfDate(LocalDate.now())
                .assetAccounts(assetDtos)
                .totalAssets(totalAssets)
                .liabilityAccounts(liabilityDtos)
                .totalLiabilities(totalLiabilities)
                .netEndowmentEquity(netEndowmentEquity)
                .totalLiabilitiesAndEquity(totalLiabilitiesAndEquity)
                .isBalanced(isBalanced)
                .build();
    }

    /**
     * 2. Profit & Loss Account: Donation revenues minus campaign marketing and event costs.
     */
    @Transactional(readOnly = true)
    public ProfitLossReportDto getProfitLoss() {
        List<ChartOfAccounts> revenues = accountRepository.findByType(AccountType.INCOME);
        List<ChartOfAccounts> expenses = accountRepository.findByType(AccountType.EXPENSE);

        List<AccountBalanceDto> revenueDtos = revenues.stream()
                .map(this::toAccountBalanceDto)
                .collect(Collectors.toList());

        List<AccountBalanceDto> expenseDtos = expenses.stream()
                .map(this::toAccountBalanceDto)
                .collect(Collectors.toList());

        BigDecimal totalRevenue = revenues.stream()
                .map(ChartOfAccounts::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenses.stream()
                .map(ChartOfAccounts::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netSurplus = totalRevenue.subtract(totalExpenses);

        double margin = 0.0;
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            margin = netSurplus.divide(totalRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .doubleValue();
        }

        return ProfitLossReportDto.builder()
                .fromDate(LocalDate.now().withDayOfYear(1))
                .toDate(LocalDate.now())
                .revenueAccounts(revenueDtos)
                .totalRevenue(totalRevenue)
                .expenseAccounts(expenseDtos)
                .totalExpenses(totalExpenses)
                .netSurplus(netSurplus)
                .operatingMarginPercentage(margin)
                .build();
    }

    /**
     * 3. Budget Report: Campaign budget vs. actual net collections report.
     * Analytic Account: Campaign Project (e.g., 2026 Library Science Expansion Campaign).
     * Budget: Campaign target collections vs. actual fundraising expenses and net contributions.
     */
    @Transactional(readOnly = true)
    public BudgetReportDto getBudgetReport() {
        List<Campaign> campaigns = campaignRepository.findAll();
        List<CampaignBudgetItemDto> budgetItems = new ArrayList<>();

        BigDecimal totalTarget = BigDecimal.ZERO;
        BigDecimal totalActual = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;

        for (Campaign c : campaigns) {
            BigDecimal target = c.getTargetAmount();
            BigDecimal actualCollections = donationRepository.sumDonationsByCampaignId(c.getId());
            BigDecimal expenses = billRepository.sumAmountByCampaignId(c.getId());
            BigDecimal netContributions = actualCollections.subtract(expenses);
            BigDecimal variance = actualCollections.subtract(target);

            double percentage = 0.0;
            if (target.compareTo(BigDecimal.ZERO) > 0) {
                percentage = actualCollections.divide(target, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .doubleValue();
            }

            String status = "IN_PROGRESS";
            if (actualCollections.compareTo(target) >= 0) {
                status = "TARGET_EXCEEDED";
            } else if (netContributions.compareTo(BigDecimal.ZERO) < 0) {
                status = "DEFICIT_WARNING";
            }

            budgetItems.add(CampaignBudgetItemDto.builder()
                    .campaignId(c.getId())
                    .campaignName(c.getName())
                    .targetCollections(target)
                    .actualCollections(actualCollections)
                    .campaignExpenses(expenses)
                    .netContributions(netContributions)
                    .targetVariance(variance)
                    .collectionPercentage(percentage)
                    .budgetStatus(status)
                    .build());

            totalTarget = totalTarget.add(target);
            totalActual = totalActual.add(actualCollections);
            totalExpenses = totalExpenses.add(expenses);
            totalNet = totalNet.add(netContributions);
        }

        BigDecimal overallVariance = totalActual.subtract(totalTarget);

        return BudgetReportDto.builder()
                .generatedDate(LocalDate.now())
                .campaignBudgets(budgetItems)
                .totalTargetCollections(totalTarget)
                .totalActualCollections(totalActual)
                .totalCampaignExpenses(totalExpenses)
                .totalNetContributions(totalNet)
                .totalVariance(overallVariance)
                .build();
    }

    private AccountBalanceDto toAccountBalanceDto(ChartOfAccounts acc) {
        return AccountBalanceDto.builder()
                .id(acc.getId())
                .code(acc.getCode())
                .name(acc.getName())
                .type(acc.getType())
                .balance(acc.getBalance())
                .build();
    }
}
