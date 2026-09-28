package com.alumni.portal;

import com.alumni.portal.dto.request.AlumniRequestDto;
import com.alumni.portal.dto.request.DonationRequestDto;
import com.alumni.portal.dto.response.AlumniResponseDto;
import com.alumni.portal.dto.response.BalanceSheetReportDto;
import com.alumni.portal.dto.response.DonationReceiptDto;
import com.alumni.portal.dto.response.ProfitLossReportDto;
import com.alumni.portal.entity.Campaign;
import com.alumni.portal.enums.PaymentMethod;
import com.alumni.portal.enums.RankingTier;
import com.alumni.portal.repository.CampaignRepository;
import com.alumni.portal.service.AlumniService;
import com.alumni.portal.service.DonationService;
import com.alumni.portal.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AlumniPortalApplicationTests {

    @Autowired
    private AlumniService alumniService;

    @Autowired
    private DonationService donationService;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ReportService reportService;

    @Test
    @DisplayName("Context Loads Successfully")
    void contextLoads() {
        assertNotNull(alumniService);
        assertNotNull(donationService);
    }

    @Test
    @DisplayName("Register Alumni and verify ranking tier calculation")
    void testRegisterAlumniAndSearch() {
        AlumniRequestDto req = AlumniRequestDto.builder()
                .name("Alex Mercer")
                .email("alex.mercer@testalum.edu")
                .batch("Class of 2017")
                .contact("+1-555-9090")
                .build();

        AlumniResponseDto created = alumniService.registerAlumni(req);
        assertNotNull(created.getId());
        assertEquals("Alex Mercer", created.getName());
        assertEquals(RankingTier.BRONZE, created.getRankingTier());
        assertEquals(BigDecimal.ZERO, created.getDonationTotal());

        List<AlumniResponseDto> searchResults = alumniService.searchAndRankAlumni("Mercer");
        assertFalse(searchResults.isEmpty());
        assertEquals("Alex Mercer", searchResults.get(0).getName());
    }

    @Test
    @DisplayName("Record Donation, verify double-entry journal creation and tier elevation to GOLD")
    void testRecordDonationAndLedgerEntry() {
        // Register a new alumni
        AlumniRequestDto req = AlumniRequestDto.builder()
                .name("Victoria Sterling")
                .email("victoria.sterling@testalum.edu")
                .batch("Class of 2012")
                .contact("+1-555-8080")
                .build();
        AlumniResponseDto alumni = alumniService.registerAlumni(req);

        // Fetch or create campaign
        Campaign campaign = campaignRepository.findAll().stream().findFirst().orElseGet(() -> {
            Campaign testCampaign = new Campaign();
            testCampaign.setName("Test Science Wing");
            testCampaign.setTargetAmount(new BigDecimal("100000.00"));
            testCampaign.setCollectedAmount(BigDecimal.ZERO);
            testCampaign.setStatus("ACTIVE");
            return campaignRepository.save(testCampaign);
        });

        BigDecimal initialCollected = campaign.getCollectedAmount();

        // Record a $25,000 donation (qualifies for GOLD tier: >= $20,000)
        DonationRequestDto donationReq = DonationRequestDto.builder()
                .alumniId(alumni.getId())
                .campaignId(campaign.getId())
                .amount(new BigDecimal("25000.00"))
                .paymentMethod(PaymentMethod.BANK_TRANSFER)
                .notes("Endowment pledge fulfillment")
                .build();

        DonationReceiptDto receipt = donationService.recordDonation(donationReq);

        assertNotNull(receipt.getReceiptNumber());
        assertEquals(new BigDecimal("25000.00"), receipt.getAmount());
        assertNotNull(receipt.getJournalEntryId());
        assertEquals("Endowment Cash & Bank", receipt.getDebitAccountName());
        assertEquals("Alumni Donations Revenue", receipt.getCreditAccountName());

        // Verify Alumni cumulative total & ranking tier upgraded
        AlumniResponseDto updatedAlumni = alumniService.getAlumniById(alumni.getId());
        assertEquals(new BigDecimal("25000.00"), updatedAlumni.getDonationTotal());
        assertEquals(RankingTier.GOLD, updatedAlumni.getRankingTier());

        // Verify Campaign collected total increased
        Campaign updatedCampaign = campaignRepository.findById(campaign.getId()).orElseThrow();
        assertEquals(initialCollected.add(new BigDecimal("25000.00")), updatedCampaign.getCollectedAmount());
    }

    @Test
    @DisplayName("Verify Balance Sheet and Profit & Loss report calculations")
    void testFinancialReports() {
        BalanceSheetReportDto balanceSheet = reportService.getBalanceSheet();
        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getTotalAssets());
        assertNotNull(balanceSheet.getTotalLiabilities());
        assertNotNull(balanceSheet.getNetEndowmentEquity());
        assertTrue(balanceSheet.isBalanced());

        ProfitLossReportDto profitLoss = reportService.getProfitLoss();
        assertNotNull(profitLoss);
        assertNotNull(profitLoss.getTotalRevenue());
        assertNotNull(profitLoss.getTotalExpenses());
        assertNotNull(profitLoss.getNetSurplus());
    }
}
