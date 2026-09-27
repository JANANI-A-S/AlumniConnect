package com.alumni.portal.controller;

import com.alumni.portal.dto.response.ApiResponse;
import com.alumni.portal.dto.response.BalanceSheetReportDto;
import com.alumni.portal.dto.response.BudgetReportDto;
import com.alumni.portal.dto.response.ProfitLossReportDto;
import com.alumni.portal.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/balance-sheet")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<BalanceSheetReportDto>> getBalanceSheet() {
        BalanceSheetReportDto report = reportService.getBalanceSheet();
        return ResponseEntity.ok(ApiResponse.ok("Endowment Balance Sheet generated", report));
    }

    @GetMapping("/profit-loss")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<ProfitLossReportDto>> getProfitLoss() {
        ProfitLossReportDto report = reportService.getProfitLoss();
        return ResponseEntity.ok(ApiResponse.ok("Profit & Loss Statement generated", report));
    }

    @GetMapping("/budget")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<BudgetReportDto>> getBudgetReport() {
        BudgetReportDto report = reportService.getBudgetReport();
        return ResponseEntity.ok(ApiResponse.ok("Campaign Budget Analytics Report generated", report));
    }
}
