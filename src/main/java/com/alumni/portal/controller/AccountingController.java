package com.alumni.portal.controller;

import com.alumni.portal.dto.request.ChartOfAccountsRequestDto;
import com.alumni.portal.dto.response.AccountBalanceDto;
import com.alumni.portal.dto.response.ApiResponse;
import com.alumni.portal.entity.ChartOfAccounts;
import com.alumni.portal.entity.Journal;
import com.alumni.portal.entity.JournalEntry;
import com.alumni.portal.service.AccountingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountingController {

    private final AccountingService accountingService;

    @GetMapping("/chart-of-accounts")
    public ResponseEntity<ApiResponse<List<AccountBalanceDto>>> getChartOfAccounts() {
        List<AccountBalanceDto> accounts = accountingService.getAllAccounts();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved Chart of Accounts", accounts));
    }

    @PostMapping("/chart-of-accounts")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> createAccount(@Valid @RequestBody ChartOfAccountsRequestDto dto) {
        ChartOfAccounts created = accountingService.createAccount(dto);
        return new ResponseEntity<>(ApiResponse.ok("Chart of Accounts entry created", created), HttpStatus.CREATED);
    }

    @GetMapping("/journals")
    public ResponseEntity<ApiResponse<List<Journal>>> getJournals() {
        List<Journal> journals = accountingService.getAllJournals();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved Journals", journals));
    }

    @GetMapping("/journal-entries")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getJournalEntries() {
        List<JournalEntry> entries = accountingService.getAllJournalEntries();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved Double-Entry General Ledger Entries", entries));
    }
}
