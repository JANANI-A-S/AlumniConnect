package com.alumni.portal.controller;

import com.alumni.portal.dto.request.PaymentRequestDto;
import com.alumni.portal.dto.request.PurchaseOrderRequestDto;
import com.alumni.portal.dto.request.VendorBillRequestDto;
import com.alumni.portal.dto.response.ApiResponse;
import com.alumni.portal.entity.Payment;
import com.alumni.portal.entity.PurchaseOrder;
import com.alumni.portal.entity.VendorBill;
import com.alumni.portal.service.ProcurementService;
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
public class ProcurementController {

    private final ProcurementService procurementService;

    @PostMapping("/purchase-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> createPurchaseOrder(@Valid @RequestBody PurchaseOrderRequestDto dto) {
        PurchaseOrder po = procurementService.createPurchaseOrder(dto);
        return new ResponseEntity<>(ApiResponse.ok("Purchase Order created successfully", po), HttpStatus.CREATED);
    }

    @GetMapping("/purchase-orders")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getAllPurchaseOrders() {
        List<PurchaseOrder> pos = procurementService.getAllPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved Purchase Orders", pos));
    }

    @PostMapping("/vendor-bills")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<VendorBill>> createVendorBill(@Valid @RequestBody VendorBillRequestDto dto) {
        VendorBill bill = procurementService.createVendorBill(dto);
        return new ResponseEntity<>(ApiResponse.ok("Vendor Bill created and posted to Purchase Journal", bill), HttpStatus.CREATED);
    }

    @GetMapping("/vendor-bills")
    public ResponseEntity<ApiResponse<List<VendorBill>>> getAllVendorBills() {
        List<VendorBill> bills = procurementService.getAllVendorBills();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved Vendor Bills", bills));
    }

    @PostMapping("/payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<Payment>> recordPayment(@Valid @RequestBody PaymentRequestDto dto) {
        Payment payment = procurementService.recordPayment(dto);
        return new ResponseEntity<>(ApiResponse.ok("Vendor Payment processed and posted to Bank Journal", payment), HttpStatus.CREATED);
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPayments() {
        List<Payment> payments = procurementService.getAllPayments();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved Payments", payments));
    }
}
