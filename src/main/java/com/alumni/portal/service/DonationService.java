package com.alumni.portal.service;

import com.alumni.portal.dto.request.DonationRequestDto;
import com.alumni.portal.dto.response.DonationReceiptDto;
import com.alumni.portal.entity.*;
import com.alumni.portal.enums.AccountType;
import com.alumni.portal.enums.JournalType;
import com.alumni.portal.exception.ResourceNotFoundException;
import com.alumni.portal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final AlumniRepository alumniRepository;
    private final CampaignRepository campaignRepository;
    private final ProductRepository productRepository;
    private final BudgetRepository budgetRepository;
    private final ChartOfAccountsRepository accountRepository;
    private final JournalRepository journalRepository;
    private final AccountingService accountingService;

    @Transactional
    public DonationReceiptDto recordDonation(DonationRequestDto dto) {
        Alumni alumni = alumniRepository.findById(dto.getAlumniId())
                .orElseThrow(() -> new ResourceNotFoundException("Alumni not found with ID: " + dto.getAlumniId()));

        Campaign campaign = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + dto.getCampaignId()));

        Product product = null;
        if (dto.getProductId() != null) {
            product = productRepository.findById(dto.getProductId()).orElse(null);
        }

        // Generate unique formal receipt number
        String receiptNumber = "REC-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        // Ensure Chart of Accounts for Bank and Donation Revenue exist
        ChartOfAccounts bankAccount = accountRepository.findByCode("1010")
                .orElseGet(() -> {
                    ChartOfAccounts account = new ChartOfAccounts();
                    account.setCode("1010");
                    account.setName("Endowment Cash & Bank");
                    account.setType(AccountType.ASSET);
                    account.setDescription("Primary bank account for institutional endowment");
                    return accountRepository.save(account);
                });

        ChartOfAccounts donationRevenueAccount = accountRepository.findByCode("4010")
                .orElseGet(() -> {
                    ChartOfAccounts account = new ChartOfAccounts();
                    account.setCode("4010");
                    account.setName("Alumni Donations Revenue");
                    account.setType(AccountType.INCOME);
                    account.setDescription("Revenue from alumni endowment contributions");
                    return accountRepository.save(account);
                });

        // Ensure Grants/Donations Journal exists
        Journal donationsJournal = journalRepository.findByType(JournalType.GRANTS_DONATIONS)
                .orElseGet(() -> {
                    Journal journal = new Journal();
                    journal.setCode("JRN-DONATION");
                    journal.setName("Grants & Donations Journal");
                    journal.setType(JournalType.GRANTS_DONATIONS);
                    journal.setDescription("Journal for recording incoming alumni gifts and endowment donations");
                    return journalRepository.save(journal);
                });

        // Post Double-Entry Journal Entry: Debit Bank, Credit Donation Revenue
        JournalEntry journalEntry = accountingService.recordJournalEntry(
                donationsJournal,
                bankAccount,
                donationRevenueAccount,
                dto.getAmount(),
                "Donation received from " + alumni.getName() + " for " + campaign.getName(),
                receiptNumber,
                campaign
        );

        // Save Donation
        Donation donation = new Donation();
        donation.setAlumni(alumni);
        donation.setCampaign(campaign);
        donation.setProduct(product);
        donation.setAmount(dto.getAmount());
        donation.setDonationDate(LocalDateTime.now());
        donation.setPaymentMethod(dto.getPaymentMethod());
        donation.setReceiptNumber(receiptNumber);
        donation.setNotes(dto.getNotes());
        donation.setJournalEntry(journalEntry);
        Donation savedDonation = donationRepository.save(donation);

        // Update Alumni Cumulative Total & Ranking Tier
        alumni.setDonationTotal(alumni.getDonationTotal().add(dto.getAmount()));
        alumni.recalculateTier();
        alumniRepository.save(alumni);

        // Update Campaign Collected Amount
        campaign.setCollectedAmount(campaign.getCollectedAmount().add(dto.getAmount()));
        campaignRepository.save(campaign);

        // Update Budget tracking if available
        budgetRepository.findByCampaignId(campaign.getId()).ifPresent(budget -> {
            budget.setActualAmount(campaign.getCollectedAmount());
            budgetRepository.save(budget);
        });

        return toReceiptDto(savedDonation, journalEntry);
    }

    @Transactional(readOnly = true)
    public List<DonationReceiptDto> getAllDonations() {
        return donationRepository.findAllByOrderByDonationDateDesc()
                .stream()
                .map(d -> toReceiptDto(d, d.getJournalEntry()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DonationReceiptDto> getDonationsByAlumni(Long alumniId) {
        return donationRepository.findByAlumniIdOrderByDonationDateDesc(alumniId)
                .stream()
                .map(d -> toReceiptDto(d, d.getJournalEntry()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DonationReceiptDto getDonationByReceiptNumber(String receiptNumber) {
        Donation donation = donationRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with receipt number: " + receiptNumber));
        return toReceiptDto(donation, donation.getJournalEntry());
    }

    @Transactional(readOnly = true)
    public DonationReceiptDto getDonationById(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with ID: " + id));
        return toReceiptDto(donation, donation.getJournalEntry());
    }

    private DonationReceiptDto toReceiptDto(Donation d, JournalEntry je) {
        return DonationReceiptDto.builder()
                .receiptNumber(d.getReceiptNumber())
                .donationId(d.getId())
                .alumniId(d.getAlumni().getId())
                .alumniName(d.getAlumni().getName())
                .alumniEmail(d.getAlumni().getEmail())
                .campaignId(d.getCampaign().getId())
                .campaignName(d.getCampaign().getName())
                .productName(d.getProduct() != null ? d.getProduct().getName() : "Direct Endowment Contribution")
                .amount(d.getAmount())
                .donationDate(d.getDonationDate())
                .paymentMethod(d.getPaymentMethod())
                .notes(d.getNotes())
                .journalEntryId(je != null ? je.getId() : null)
                .debitAccountName(je != null ? je.getDebitAccount().getName() : "Endowment Cash & Bank")
                .creditAccountName(je != null ? je.getCreditAccount().getName() : "Alumni Donations Revenue")
                .accountingStatus("POSTED_TO_LEDGER")
                .build();
    }
}
