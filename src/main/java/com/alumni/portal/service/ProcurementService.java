package com.alumni.portal.service;

import com.alumni.portal.dto.request.PaymentRequestDto;
import com.alumni.portal.dto.request.PurchaseOrderRequestDto;
import com.alumni.portal.dto.request.VendorBillRequestDto;
import com.alumni.portal.entity.*;
import com.alumni.portal.enums.AccountType;
import com.alumni.portal.enums.BillStatus;
import com.alumni.portal.enums.JournalType;
import com.alumni.portal.enums.POStatus;
import com.alumni.portal.exception.BadRequestException;
import com.alumni.portal.exception.ResourceNotFoundException;
import com.alumni.portal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcurementService {

    private final PurchaseOrderRepository poRepository;
    private final VendorBillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final ContactRepository contactRepository;
    private final CampaignRepository campaignRepository;
    private final ChartOfAccountsRepository accountRepository;
    private final JournalRepository journalRepository;
    private final BudgetRepository budgetRepository;
    private final AccountingService accountingService;

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequestDto dto) {
        Contact vendor = contactRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID: " + dto.getVendorId()));

        Campaign campaign = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + dto.getCampaignId()));

        String poNumber = "PO-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
                + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber(poNumber);
        po.setVendor(vendor);
        po.setCampaign(campaign);
        po.setAmount(dto.getAmount());
        po.setOrderDate(LocalDate.now());
        po.setStatus(POStatus.APPROVED);
        po.setDescription(dto.getDescription());

        return poRepository.save(po);
    }

    @Transactional
    public VendorBill createVendorBill(VendorBillRequestDto dto) {
        Contact vendor = contactRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID: " + dto.getVendorId()));

        PurchaseOrder po = null;
        Campaign campaign = null;

        if (dto.getPurchaseOrderId() != null) {
            po = poRepository.findById(dto.getPurchaseOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("PO not found with ID: " + dto.getPurchaseOrderId()));
            campaign = po.getCampaign();
            po.setStatus(POStatus.BILLED);
            poRepository.save(po);
        } else if (dto.getCampaignId() != null) {
            campaign = campaignRepository.findById(dto.getCampaignId()).orElse(null);
        }

        String billNumber = "BILL-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
                + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        VendorBill bill = new VendorBill();
        bill.setBillNumber(billNumber);
        bill.setPurchaseOrder(po);
        bill.setVendor(vendor);
        bill.setCampaign(campaign);
        bill.setAmount(dto.getAmount());
        bill.setBillDate(LocalDate.now());
        bill.setDueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.UNPAID);
        bill.setNotes(dto.getNotes());
        VendorBill savedBill = billRepository.save(bill);

        // Ensure Chart of Accounts: Expense Account (5010) and Vendor Payables (2010)
        ChartOfAccounts expenseAccount = accountRepository.findByCode("5010")
                .orElseGet(() -> {
                    ChartOfAccounts account = new ChartOfAccounts();
                    account.setCode("5010");
                    account.setName("Alumni Event & Campaign Expenses");
                    account.setType(AccountType.EXPENSE);
                    account.setDescription("Expenses incurred for gala events, marketing, and institutional campaigns");
                    return accountRepository.save(account);
                });

        ChartOfAccounts payablesAccount = accountRepository.findByCode("2010")
                .orElseGet(() -> {
                    ChartOfAccounts account = new ChartOfAccounts();
                    account.setCode("2010");
                    account.setName("Vendor Payables");
                    account.setType(AccountType.LIABILITY);
                    account.setDescription("Outstanding liabilities owed to vendors and marketing agencies");
                    return accountRepository.save(account);
                });

        // Ensure Purchase Journal
        Journal purchaseJournal = journalRepository.findByType(JournalType.PURCHASE)
                .orElseGet(() -> {
                    Journal journal = new Journal();
                    journal.setCode("JRN-PURCHASE");
                    journal.setName("Purchase Journal");
                    journal.setType(JournalType.PURCHASE);
                    journal.setDescription("Journal for recording vendor bills and campaign procurement liabilities");
                    return journalRepository.save(journal);
                });

        // Post Double-Entry Journal: Debit Event Expense, Credit Vendor Payables
        accountingService.recordJournalEntry(
                purchaseJournal,
                expenseAccount,
                payablesAccount,
                dto.getAmount(),
                "Vendor bill recorded for " + vendor.getName() + " - " + billNumber,
                billNumber,
                campaign
        );

        // Update Budget actual expenses if campaign is present
        if (campaign != null) {
            budgetRepository.findByCampaignId(campaign.getId()).ifPresent(b -> {
                b.setActualExpenses(b.getActualExpenses().add(dto.getAmount()));
                budgetRepository.save(b);
            });
        }

        return savedBill;
    }

    @Transactional
    public Payment recordPayment(PaymentRequestDto dto) {
        VendorBill bill = billRepository.findById(dto.getVendorBillId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor Bill not found with ID: " + dto.getVendorBillId()));

        if (bill.getStatus() == BillStatus.PAID) {
            throw new BadRequestException("Vendor Bill " + bill.getBillNumber() + " is already fully paid.");
        }

        String paymentNumber = "PAY-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
                + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Payment payment = new Payment();
        payment.setPaymentNumber(paymentNumber);
        payment.setVendorBill(bill);
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setReference(dto.getReference());
        Payment savedPayment = paymentRepository.save(payment);

        // Mark Bill as PAID
        bill.setStatus(BillStatus.PAID);
        billRepository.save(bill);

        // Accounts: Debit Vendor Payables (LIABILITY), Credit Endowment Cash & Bank (ASSET)
        ChartOfAccounts payablesAccount = accountRepository.findByCode("2010")
                .orElseThrow(() -> new ResourceNotFoundException("Vendor Payables account not found."));
        ChartOfAccounts bankAccount = accountRepository.findByCode("1010")
                .orElseThrow(() -> new ResourceNotFoundException("Endowment Cash & Bank account not found."));

        Journal bankJournal = journalRepository.findByType(JournalType.BANK_CASH)
                .orElseGet(() -> {
                    Journal journal = new Journal();
                    journal.setCode("JRN-BANK");
                    journal.setName("Bank & Cash Journal");
                    journal.setType(JournalType.BANK_CASH);
                    journal.setDescription("Journal for recording disbursements and cash receipts");
                    return journalRepository.save(journal);
                });

        // Double-Entry: Debit Payables (reduces liability), Credit Bank (reduces asset)
        accountingService.recordJournalEntry(
                bankJournal,
                payablesAccount,
                bankAccount,
                dto.getAmount(),
                "Disbursement to " + bill.getVendor().getName() + " for bill " + bill.getBillNumber(),
                paymentNumber,
                bill.getCampaign()
        );

        return savedPayment;
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return poRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<VendorBill> getAllVendorBills() {
        return billRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
