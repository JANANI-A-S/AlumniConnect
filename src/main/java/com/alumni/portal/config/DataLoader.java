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

        ChartOfAccounts bank = new ChartOfAccounts();
        bank.setCode("1010");
        bank.setName("Endowment Cash & Bank");
        bank.setType(AccountType.ASSET);
        bank.setBalance(new BigDecimal("87500.00"));
        bank.setDescription("Primary endowment checking and cash reserve account");
        bank = accountRepository.save(bank);

        ChartOfAccounts investments = new ChartOfAccounts();
        investments.setCode("1020");
        investments.setName("Endowment Term Investments");
        investments.setType(AccountType.ASSET);
        investments.setBalance(new BigDecimal("250000.00"));
        investments.setDescription("Fixed institutional long-term endowment investments");
        investments = accountRepository.save(investments);

        ChartOfAccounts payables = new ChartOfAccounts();
        payables.setCode("2010");
        payables.setName("Vendor Payables");
        payables.setType(AccountType.LIABILITY);
        payables.setBalance(new BigDecimal("12000.00"));
        payables.setDescription("Liabilities owed to event vendors and suppliers");
        payables = accountRepository.save(payables);

        ChartOfAccounts donationsRevenue = new ChartOfAccounts();
        donationsRevenue.setCode("4010");
        donationsRevenue.setName("Alumni Donations Revenue");
        donationsRevenue.setType(AccountType.INCOME);
        donationsRevenue.setBalance(new BigDecimal("87500.00"));
        donationsRevenue.setDescription("Cumulative donations received for university endowments");
        donationsRevenue = accountRepository.save(donationsRevenue);

        ChartOfAccounts galaReceipts = new ChartOfAccounts();
        galaReceipts.setCode("4020");
        galaReceipts.setName("Alumni Gala Event Receipts");
        galaReceipts.setType(AccountType.INCOME);
        galaReceipts.setBalance(new BigDecimal("15000.00"));
        galaReceipts.setDescription("Ticket and sponsorship receipts from institutional galas");
        accountRepository.save(galaReceipts);

        ChartOfAccounts eventExpenses = new ChartOfAccounts();
        eventExpenses.setCode("5010");
        eventExpenses.setName("Alumni Event & Campaign Expenses");
        eventExpenses.setType(AccountType.EXPENSE);
        eventExpenses.setBalance(new BigDecimal("12000.00"));
        eventExpenses.setDescription("Operational expenses for alumni galas and campaign rallies");
        eventExpenses = accountRepository.save(eventExpenses);

        ChartOfAccounts outreachExpenses = new ChartOfAccounts();
        outreachExpenses.setCode("5020");
        outreachExpenses.setName("Marketing & Outreach Expenses");
        outreachExpenses.setType(AccountType.EXPENSE);
        outreachExpenses.setBalance(new BigDecimal("4500.00"));
        outreachExpenses.setDescription("Campaign outreach, digital mailers, and print publications");
        accountRepository.save(outreachExpenses);

        Journal donationJournal = new Journal();
        donationJournal.setCode("JRN-DONATION");
        donationJournal.setName("Grants & Donations Journal");
        donationJournal.setType(JournalType.GRANTS_DONATIONS);
        donationJournal.setDescription("Journal for recording incoming alumni gifts and endowment donations");
        donationJournal = journalRepository.save(donationJournal);

        Journal purchaseJournal = new Journal();
        purchaseJournal.setCode("JRN-PURCHASE");
        purchaseJournal.setName("Purchase Journal");
        purchaseJournal.setType(JournalType.PURCHASE);
        purchaseJournal.setDescription("Journal for recording vendor bills, procurement, and accounts payable");
        purchaseJournal = journalRepository.save(purchaseJournal);

        Journal bankJournal = new Journal();
        bankJournal.setCode("JRN-BANK");
        bankJournal.setName("Bank & Cash Journal");
        bankJournal.setType(JournalType.BANK_CASH);
        bankJournal.setDescription("Journal for university cash, wire disbursements, and bank reconciliation");
        journalRepository.save(bankJournal);

        Contact donorContact = new Contact();
        donorContact.setName("Dr. Eleanor Vance");
        donorContact.setEmail("eleanor.vance@mit-alum.edu");
        donorContact.setPhone("+1-617-555-0199");
        donorContact.setOrganization("Vance Tech Ventures");
        donorContact.setContactType(ContactType.DONOR);
        donorContact = contactRepository.save(donorContact);

        Contact vendorContact = new Contact();
        vendorContact.setName("Gala Gourmet Catering Co.");
        vendorContact.setEmail("contact@galagourmet.com");
        vendorContact.setPhone("+1-212-555-0144");
        vendorContact.setOrganization("Gourmet Hospitality LLC");
        vendorContact.setContactType(ContactType.VENDOR);
        vendorContact = contactRepository.save(vendorContact);

        Contact agencyContact = new Contact();
        agencyContact.setName("Horizon Media Agency");
        agencyContact.setEmail("campaigns@horizonmedia.org");
        agencyContact.setPhone("+1-415-555-0182");
        agencyContact.setOrganization("Horizon Global Communications");
        agencyContact.setContactType(ContactType.AGENCY);
        contactRepository.save(agencyContact);

        Product prodEndowment = new Product();
        prodEndowment.setName("Endowment Contribution");
        prodEndowment.setProductType(ProductType.ENDOWMENT_CONTRIBUTION);
        prodEndowment.setPrice(new BigDecimal("5000.00"));
        prodEndowment.setDescription("Direct institutional contribution to university capital endowment");
        prodEndowment = productRepository.save(prodEndowment);

        Product galaTicket = new Product();
        galaTicket.setName("Alumni Gala Ticket");
        galaTicket.setProductType(ProductType.GALA_TICKET);
        galaTicket.setPrice(new BigDecimal("250.00"));
        galaTicket.setDescription("Ticket entry for annual alumni black-tie benefit dinner and gala");
        productRepository.save(galaTicket);

        Product mentorshipSponsorship = new Product();
        mentorshipSponsorship.setName("Campus Mentorship Sponsorship");
        mentorshipSponsorship.setProductType(ProductType.MENTORSHIP_SPONSORSHIP);
        mentorshipSponsorship.setPrice(new BigDecimal("1500.00"));
        mentorshipSponsorship.setDescription("Semester-long sponsorship funding student-alumni mentorship programs");
        productRepository.save(mentorshipSponsorship);

        Campaign campaign1 = new Campaign();
        campaign1.setName("2026 Library Science Expansion Campaign");
        campaign1.setDescription("Funding state-of-the-art digital archives, quiet study chambers, and AI research labs");
        campaign1.setTargetAmount(new BigDecimal("250000.00"));
        campaign1.setCollectedAmount(new BigDecimal("55000.00"));
        campaign1.setStartDate(LocalDate.of(2026, 1, 1));
        campaign1.setEndDate(LocalDate.of(2026, 12, 31));
        campaign1.setStatus("ACTIVE");
        campaign1 = campaignRepository.save(campaign1);

        Campaign campaign2 = new Campaign();
        campaign2.setName("STEM Research & Innovation Endowment");
        campaign2.setDescription("Endowed professorships and advanced lab equipment for student discovery");
        campaign2.setTargetAmount(new BigDecimal("500000.00"));
        campaign2.setCollectedAmount(new BigDecimal("32500.00"));
        campaign2.setStartDate(LocalDate.of(2026, 2, 15));
        campaign2.setEndDate(LocalDate.of(2027, 2, 15));
        campaign2.setStatus("ACTIVE");
        campaign2 = campaignRepository.save(campaign2);

        Alumni alumni1 = new Alumni();
        alumni1.setName("Dr. Eleanor Vance");
        alumni1.setEmail("eleanor.vance@mit-alum.edu");
        alumni1.setBatch("Class of 2010");
        alumni1.setContact("+1-617-555-0199");
        alumni1.setDonationTotal(new BigDecimal("55000.00"));
        alumni1.setRankingTier(RankingTier.PLATINUM);
        alumni1 = alumniRepository.save(alumni1);

        Alumni alumni2 = new Alumni();
        alumni2.setName("Marcus Aurelius Sterling");
        alumni2.setEmail("marcus.sterling@alum.org");
        alumni2.setBatch("Class of 2014");
        alumni2.setContact("+1-312-555-0177");
        alumni2.setDonationTotal(new BigDecimal("25000.00"));
        alumni2.setRankingTier(RankingTier.GOLD);
        alumni2 = alumniRepository.save(alumni2);

        Alumni alumni3 = new Alumni();
        alumni3.setName("Sarah Jenkins");
        alumni3.setEmail("sarah.jenkins@alum.org");
        alumni3.setBatch("Class of 2018");
        alumni3.setContact("+1-206-555-0122");
        alumni3.setDonationTotal(new BigDecimal("7500.00"));
        alumni3.setRankingTier(RankingTier.SILVER);
        alumniRepository.save(alumni3);

        Alumni alumni4 = new Alumni();
        alumni4.setName("David Zhao");
        alumni4.setEmail("david.zhao@alum.org");
        alumni4.setBatch("Class of 2021");
        alumni4.setContact("+1-408-555-0133");
        alumni4.setDonationTotal(new BigDecimal("1200.00"));
        alumni4.setRankingTier(RankingTier.BRONZE);
        alumniRepository.save(alumni4);

        Alumni alumni5 = new Alumni();
        alumni5.setName("Priya Patel");
        alumni5.setEmail("priya.patel@alum.org");
        alumni5.setBatch("Class of 2023");
        alumni5.setContact("+1-646-555-0144");
        alumni5.setDonationTotal(BigDecimal.ZERO);
        alumni5.setRankingTier(RankingTier.BRONZE);
        alumniRepository.save(alumni5);

        Budget budget1 = new Budget();
        budget1.setCampaign(campaign1);
        budget1.setTargetAmount(new BigDecimal("250000.00"));
        budget1.setActualAmount(new BigDecimal("55000.00"));
        budget1.setAllocatedExpenseBudget(new BigDecimal("37500.00"));
        budget1.setActualExpenses(new BigDecimal("12000.00"));
        budget1.setNotes("2026 Library Science Expansion Budget with 15% marketing cap");
        budgetRepository.save(budget1);

        Budget budget2 = new Budget();
        budget2.setCampaign(campaign2);
        budget2.setTargetAmount(new BigDecimal("500000.00"));
        budget2.setActualAmount(new BigDecimal("32500.00"));
        budget2.setAllocatedExpenseBudget(new BigDecimal("75000.00"));
        budget2.setActualExpenses(BigDecimal.ZERO);
        budget2.setNotes("STEM Endowment initial phase");
        budgetRepository.save(budget2);

        JournalEntry entry1 = new JournalEntry();
        entry1.setJournal(donationJournal);
        entry1.setDebitAccount(bank);
        entry1.setCreditAccount(donationsRevenue);
        entry1.setAmount(new BigDecimal("55000.00"));
        entry1.setEntryDate(LocalDateTime.now());
        entry1.setDescription("Foundational endowment donation from Dr. Eleanor Vance");
        entry1.setReferenceId("REC-2026-INIT01");
        entry1.setCampaign(campaign1);
        entry1 = journalEntryRepository.save(entry1);

        Donation donation = new Donation();
        donation.setAlumni(alumni1);
        donation.setCampaign(campaign1);
        donation.setProduct(prodEndowment);
        donation.setAmount(new BigDecimal("55000.00"));
        donation.setDonationDate(LocalDateTime.now());
        donation.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        donation.setReceiptNumber("REC-2026-INIT01");
        donation.setNotes("Pledge fulfilled for Library Science Expansion");
        donation.setJournalEntry(entry1);
        donationRepository.save(donation);

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber("PO-2026-001");
        po.setVendor(vendorContact);
        po.setCampaign(campaign1);
        po.setAmount(new BigDecimal("12000.00"));
        po.setOrderDate(LocalDate.now());
        po.setStatus(POStatus.BILLED);
        po.setDescription("Catering & banquet service for 2026 Library Science Launch Gala");
        po = poRepository.save(po);

        VendorBill bill = new VendorBill();
        bill.setBillNumber("BILL-2026-INIT01");
        bill.setPurchaseOrder(po);
        bill.setVendor(vendorContact);
        bill.setCampaign(campaign1);
        bill.setAmount(new BigDecimal("12000.00"));
        bill.setBillDate(LocalDate.now());
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.UNPAID);
        bill.setNotes("Net 30 catering invoice");
        billRepository.save(bill);

        JournalEntry purchaseEntry = new JournalEntry();
        purchaseEntry.setJournal(purchaseJournal);
        purchaseEntry.setDebitAccount(eventExpenses);
        purchaseEntry.setCreditAccount(payables);
        purchaseEntry.setAmount(new BigDecimal("12000.00"));
        purchaseEntry.setEntryDate(LocalDateTime.now());
        purchaseEntry.setDescription("Gala catering and venue setup invoice from Gala Gourmet Catering");
        purchaseEntry.setReferenceId("BILL-2026-INIT01");
        purchaseEntry.setCampaign(campaign1);
        journalEntryRepository.save(purchaseEntry);

        log.info("Master data initialization completed successfully.");
    }
}
