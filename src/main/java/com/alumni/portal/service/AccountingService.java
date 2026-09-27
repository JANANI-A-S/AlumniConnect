package com.alumni.portal.service;

import com.alumni.portal.dto.request.ChartOfAccountsRequestDto;
import com.alumni.portal.dto.response.AccountBalanceDto;
import com.alumni.portal.entity.Campaign;
import com.alumni.portal.entity.ChartOfAccounts;
import com.alumni.portal.entity.Journal;
import com.alumni.portal.entity.JournalEntry;
import com.alumni.portal.enums.AccountType;
import com.alumni.portal.enums.JournalType;
import com.alumni.portal.exception.BadRequestException;
import com.alumni.portal.exception.ResourceNotFoundException;
import com.alumni.portal.repository.ChartOfAccountsRepository;
import com.alumni.portal.repository.JournalEntryRepository;
import com.alumni.portal.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountingService {

    private final ChartOfAccountsRepository accountRepository;
    private final JournalRepository journalRepository;
    private final JournalEntryRepository entryRepository;

    @Transactional(readOnly = true)
    public List<AccountBalanceDto> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(this::toAccountDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountBalanceDto> getAccountsByType(AccountType type) {
        return accountRepository.findByType(type)
                .stream()
                .map(this::toAccountDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChartOfAccounts createAccount(ChartOfAccountsRequestDto dto) {
        if (accountRepository.existsByCode(dto.getCode())) {
            throw new BadRequestException("Account code '" + dto.getCode() + "' already exists.");
        }
        ChartOfAccounts account = ChartOfAccounts.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .type(dto.getType())
                .balance(BigDecimal.ZERO)
                .description(dto.getDescription())
                .build();
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public ChartOfAccounts getAccountByCode(String code) {
        return accountRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public ChartOfAccounts getAccountByName(String name) {
        return accountRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with name: " + name));
    }

    @Transactional(readOnly = true)
    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Journal getJournalByType(JournalType type) {
        return journalRepository.findByType(type)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found with type: " + type));
    }

    /**
     * Executes standard double-entry transaction:
     * - Debit Account increases for ASSET and EXPENSE, decreases for LIABILITY and INCOME
     * - Credit Account increases for LIABILITY and INCOME, decreases for ASSET and EXPENSE
     */
    @Transactional
    public JournalEntry recordJournalEntry(Journal journal,
                                           ChartOfAccounts debitAccount,
                                           ChartOfAccounts creditAccount,
                                           BigDecimal amount,
                                           String description,
                                           String referenceId,
                                           Campaign campaign) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Journal entry amount must be strictly positive.");
        }

        // Apply debit effect to debitAccount
        if (debitAccount.getType() == AccountType.ASSET || debitAccount.getType() == AccountType.EXPENSE) {
            debitAccount.setBalance(debitAccount.getBalance().add(amount));
        } else {
            debitAccount.setBalance(debitAccount.getBalance().subtract(amount));
        }
        accountRepository.save(debitAccount);

        // Apply credit effect to creditAccount
        if (creditAccount.getType() == AccountType.LIABILITY || creditAccount.getType() == AccountType.INCOME) {
            creditAccount.setBalance(creditAccount.getBalance().add(amount));
        } else {
            creditAccount.setBalance(creditAccount.getBalance().subtract(amount));
        }
        accountRepository.save(creditAccount);

        JournalEntry entry = JournalEntry.builder()
                .journal(journal)
                .debitAccount(debitAccount)
                .creditAccount(creditAccount)
                .amount(amount)
                .entryDate(LocalDateTime.now())
                .description(description)
                .referenceId(referenceId)
                .campaign(campaign)
                .build();

        return entryRepository.save(entry);
    }

    @Transactional(readOnly = true)
    public List<JournalEntry> getAllJournalEntries() {
        return entryRepository.findAllByOrderByEntryDateDesc();
    }

    @Transactional(readOnly = true)
    public List<JournalEntry> getEntriesByReference(String referenceId) {
        return entryRepository.findByReferenceId(referenceId);
    }

    public AccountBalanceDto toAccountDto(ChartOfAccounts acc) {
        return AccountBalanceDto.builder()
                .id(acc.getId())
                .code(acc.getCode())
                .name(acc.getName())
                .type(acc.getType())
                .balance(acc.getBalance())
                .build();
    }
}
