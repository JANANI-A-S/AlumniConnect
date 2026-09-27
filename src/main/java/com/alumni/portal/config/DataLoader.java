package com.alumni.portal.config;

import com.alumni.portal.entity.*;
import com.alumni.portal.enums.*;
import com.alumni.portal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ChartOfAccountsRepository accountRepository;
    private final JournalRepository journalRepository;
    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final CampaignRepository campaignRepository;
    private final AlumniRepository alumniRepository;
    private final BudgetRepository budgetRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final DonationRepository donationRepository;
    private final PurchaseOrderRepository poRepository;
    private final VendorBillRepository billRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (accountRepository.count() > 0) {
            log.info("Master data already initialized.");
            return;
        }

        log.info("Initializing Master Data, CoA, Journals, Campaigns, and Seed Records...");

        // 1. Chart of Accounts
        ChartOfAccounts bank = accountRepository.save(ChartOfAccounts.builder()
                .code("1010").name("Endowment Cash & Bank").type(AccountType.ASSET)
                .balance(new BigDecimal("87500.00")).description("Primary endowment checking and cash reserve account").build());

        ChartOfAccounts investments = accountRepository.save(ChartOfAccounts.builder()
                .code("1020").name("Endowment Term Investments").type(AccountType.ASSET)
                .balance(new BigDecimal("250000.00")).description("Fixed institutional long-term endowment investments").build());

        ChartOfAccounts payables = accountRepository.save(ChartOfAccounts.builder()
                .code("2010").name("Vendor Payables").type(AccountType.LIABILITY)
                .balance(new BigDecimal("12000.00")).description("Liabilities owed to event vendors and suppliers").build());

        ChartOfAccounts donationsRevenue = accountRepository.save(ChartOfAccounts.builder()
                .code("4010").name("Alumni Donations Revenue").type(AccountType.INCOME)
                .balance(new BigDecimal("87500.00")).description("Cumulative donations received for university endowments").build());

        accountRepository.save(ChartOfAccounts.builder()
                .code("4020").name("Alumni Gala Event Receipts").type(AccountType.INCOME)
                .balance(new BigDecimal("15000.00")).description("Ticket and sponsorship receipts from institutional galas").build());

        ChartOfAccounts eventExpenses = accountRepository.save(ChartOfAccounts.builder()
                .code("5010").name("Alumni Event & Campaign Expenses").type(AccountType.EXPENSE)
                .balance(new BigDecimal("12000.00")).description("Operational expenses for alumni galas and campaign rallies").build());

        accountRepository.save(ChartOfAccounts.builder()
                .code("5020").name("Marketing & Outreach Expenses").type(AccountType.EXPENSE)
                .balance(new BigDecimal("4500.00")).description("Campaign outreach, digital mailers, and print publications").build());

        // 2. Journals
        Journal donationJournal = journalRepository.save(Journal.builder()
                .code("JRN-DONATION").name("Grants & Donations Journal").type(JournalType.GRANTS_DONATIONS)
                .description("Journal for recording incoming alumni gifts and endowment donations").build());

        Journal purchaseJournal = journalRepository.save(Journal.builder()
                .code("JRN-PURCHASE").name("Purchase Journal").type(JournalType.PURCHASE)
                .description("Journal for recording vendor bills, procurement, and accounts payable").build());

        journalRepository.save(Journal.builder()
                .code("JRN-BANK").name("Bank & Cash Journal").type(JournalType.BANK_CASH)
                .description("Journal for university cash, wire disbursements, and bank reconciliation").build());

        // 3. Contacts
        Contact donorContact = contactRepository.save(Contact.builder()
                .name("Dr. Eleanor Vance").email("eleanor.vance@mit-alum.edu").phone("+1-617-555-0199")
                .organization("Vance Tech Ventures").contactType(ContactType.DONOR).build());

        Contact vendorContact = contactRepository.save(Contact.builder()
                .name("Gala Gourmet Catering Co.").email("contact@galagourmet.com").phone("+1-212-555-0144")
                .organization("Gourmet Hospitality LLC").contactType(ContactType.VENDOR).build());

        contactRepository.save(Contact.builder()
                .name("Horizon Media Agency").email("campaigns@horizonmedia.org").phone("+1-415-555-0182")
                .organization("Horizon Global Communications").contactType(ContactType.AGENCY).build());

        // 4. Products
        Product prodEndowment = productRepository.save(Product.builder()
                .name("Endowment Contribution").productType(ProductType.ENDOWMENT_CONTRIBUTION)
                .price(new BigDecimal("5000.00")).description("Direct institutional contribution to university capital endowment").build());

        productRepository.save(Product.builder()
                .name("Alumni Gala Ticket").productType(ProductType.GALA_TICKET)
                .price(new BigDecimal("250.00")).description("Ticket entry for annual alumni black-tie benefit dinner and gala").build());

        productRepository.save(Product.builder()
                .name("Campus Mentorship Sponsorship").productType(ProductType.MENTORSHIP_SPONSORSHIP)
                .price(new BigDecimal("1500.00")).description("Semester-long sponsorship funding student-alumni mentorship programs").build());

        // 5. Campaigns
        Campaign campaign1 = campaignRepository.save(Campaign.builder()
                .name("2026 Library Science Expansion Campaign")
                .description("Funding state-of-the-art digital archives, quiet study chambers, and AI research labs")
                .targetAmount(new BigDecimal("250000.00"))
                .collectedAmount(new BigDecimal("55000.00"))
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31))
                .status("ACTIVE")
                .build());

        Campaign campaign2 = campaignRepository.save(Campaign.builder()
                .name("STEM Research & Innovation Endowment")
                .description("Endowed professorships and advanced lab equipment for student discovery")
                .targetAmount(new BigDecimal("500000.00"))
                .collectedAmount(new BigDecimal("32500.00"))
                .startDate(LocalDate.of(2026, 2, 15))
                .endDate(LocalDate.of(2027, 2, 15))
                .status("ACTIVE")
                .build());

        // 6. Alumni
        Alumni alumni1 = alumniRepository.save(Alumni.builder()
                .name("Dr. Eleanor Vance").email("eleanor.vance@mit-alum.edu").batch("Class of 2010")
                .contact("+1-617-555-0199").donationTotal(new BigDecimal("55000.00")).rankingTier(RankingTier.PLATINUM).build());

        Alumni alumni2 = alumniRepository.save(Alumni.builder()
                .name("Marcus Aurelius Sterling").email("marcus.sterling@alum.org").batch("Class of 2014")
                .contact("+1-312-555-0177").donationTotal(new BigDecimal("25000.00")).rankingTier(RankingTier.GOLD).build());

        alumniRepository.save(Alumni.builder()
                .name("Sarah Jenkins").email("sarah.jenkins@alum.org").batch("Class of 2018")
                .contact("+1-206-555-0122").donationTotal(new BigDecimal("7500.00")).rankingTier(RankingTier.SILVER).build());

        alumniRepository.save(Alumni.builder()
                .name("David Zhao").email("david.zhao@alum.org").batch("Class of 2021")
                .contact("+1-408-555-0133").donationTotal(new BigDecimal("1200.00")).rankingTier(RankingTier.BRONZE).build());

        alumniRepository.save(Alumni.builder()
                .name("Priya Patel").email("priya.patel@alum.org").batch("Class of 2023")
                .contact("+1-646-555-0144").donationTotal(BigDecimal.ZERO).rankingTier(RankingTier.BRONZE).build());

        // 7. Budgets
        budgetRepository.save(Budget.builder()
                .campaign(campaign1)
                .targetAmount(new BigDecimal("250000.00"))
                .actualAmount(new BigDecimal("55000.00"))
                .allocatedExpenseBudget(new BigDecimal("37500.00"))
                .actualExpenses(new BigDecimal("12000.00"))
                .notes("2026 Library Science Expansion Budget with 15% marketing cap")
                .build());

        budgetRepository.save(Budget.builder()
                .campaign(campaign2)
                .targetAmount(new BigDecimal("500000.00"))
                .actualAmount(new BigDecimal("32500.00"))
                .allocatedExpenseBudget(new BigDecimal("75000.00"))
                .actualExpenses(BigDecimal.ZERO)
                .notes("STEM Endowment initial phase")
                .build());

        // 8. Initial Journal Entry & Donation
        JournalEntry entry1 = journalEntryRepository.save(JournalEntry.builder()
                .journal(donationJournal)
                .debitAccount(bank)
                .creditAccount(donationsRevenue)
                .amount(new BigDecimal("55000.00"))
                .entryDate(LocalDateTime.now())
                .description("Foundational endowment donation from Dr. Eleanor Vance")
                .referenceId("REC-2026-INIT01")
                .campaign(campaign1)
                .build());

        donationRepository.save(Donation.builder()
                .alumni(alumni1)
                .campaign(campaign1)
                .product(prodEndowment)
                .amount(new BigDecimal("55000.00"))
                .donationDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.BANK_TRANSFER)
                .receiptNumber("REC-2026-INIT01")
                .notes("Pledge fulfilled for Library Science Expansion")
                .journalEntry(entry1)
                .build());

        // 9. PO and Vendor Bill
        PurchaseOrder po = poRepository.save(PurchaseOrder.builder()
                .poNumber("PO-2026-001")
                .vendor(vendorContact)
                .campaign(campaign1)
                .amount(new BigDecimal("12000.00"))
                .orderDate(LocalDate.now())
                .status(POStatus.BILLED)
                .description("Catering & banquet service for 2026 Library Science Launch Gala")
                .build());

        billRepository.save(VendorBill.builder()
                .billNumber("BILL-2026-INIT01")
                .purchaseOrder(po)
                .vendor(vendorContact)
                .campaign(campaign1)
                .amount(new BigDecimal("12000.00"))
                .billDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .status(BillStatus.UNPAID)
                .notes("Net 30 catering invoice")
                .build());

        journalEntryRepository.save(JournalEntry.builder()
                .journal(purchaseJournal)
                .debitAccount(eventExpenses)
                .creditAccount(payables)
                .amount(new BigDecimal("12000.00"))
                .entryDate(LocalDateTime.now())
                .description("Gala catering and venue setup invoice from Gala Gourmet Catering")
                .referenceId("BILL-2026-INIT01")
                .campaign(campaign1)
                .build());

        log.info("Master data initialization completed successfully.");
    }
}
