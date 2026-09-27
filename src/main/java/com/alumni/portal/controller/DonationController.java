package com.alumni.portal.controller;

import com.alumni.portal.dto.request.DonationRequestDto;
import com.alumni.portal.dto.response.ApiResponse;
import com.alumni.portal.dto.response.DonationReceiptDto;
import com.alumni.portal.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    public ResponseEntity<ApiResponse<DonationReceiptDto>> recordDonation(@Valid @RequestBody DonationRequestDto dto) {
        DonationReceiptDto receipt = donationService.recordDonation(dto);
        return new ResponseEntity<>(ApiResponse.ok("Donation processed and double-entry ledger updated", receipt), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DonationReceiptDto>>> getAllDonations() {
        List<DonationReceiptDto> donations = donationService.getAllDonations();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved all donation receipts", donations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DonationReceiptDto>> getDonationById(@PathVariable Long id) {
        DonationReceiptDto receipt = donationService.getDonationById(id);
        return ResponseEntity.ok(ApiResponse.ok("Retrieved donation receipt", receipt));
    }

    @GetMapping("/receipt/{receiptNumber}")
    public ResponseEntity<ApiResponse<DonationReceiptDto>> getDonationByReceiptNumber(@PathVariable String receiptNumber) {
        DonationReceiptDto receipt = donationService.getDonationByReceiptNumber(receiptNumber);
        return ResponseEntity.ok(ApiResponse.ok("Retrieved donation receipt", receipt));
    }

    @GetMapping("/alumni/{alumniId}")
    public ResponseEntity<ApiResponse<List<DonationReceiptDto>>> getDonationsByAlumni(@PathVariable Long alumniId) {
        List<DonationReceiptDto> donations = donationService.getDonationsByAlumni(alumniId);
        return ResponseEntity.ok(ApiResponse.ok("Retrieved alumni donation history", donations));
    }
}
