package com.alumni.portal.controller;

import com.alumni.portal.dto.request.ContactRequestDto;
import com.alumni.portal.dto.request.ProductRequestDto;
import com.alumni.portal.dto.response.ApiResponse;
import com.alumni.portal.entity.Contact;
import com.alumni.portal.entity.Product;
import com.alumni.portal.enums.ContactType;
import com.alumni.portal.enums.ProductType;
import com.alumni.portal.service.MasterDataService;
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
public class MasterDataController {

    private final MasterDataService masterDataService;

    // Contact Master: Alumni Donor, Event Catering Vendor, Marketing Agency
    @PostMapping("/contacts")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<Contact>> createContact(@Valid @RequestBody ContactRequestDto dto) {
        Contact contact = masterDataService.createContact(dto);
        return new ResponseEntity<>(ApiResponse.ok("Contact created successfully", contact), HttpStatus.CREATED);
    }

    @GetMapping("/contacts")
    public ResponseEntity<ApiResponse<List<Contact>>> getAllContacts(
            @RequestParam(required = false) ContactType type) {
        List<Contact> contacts = (type != null) ? masterDataService.getContactsByType(type) : masterDataService.getAllContacts();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved contacts", contacts));
    }

    // Product Master: Endowment Contribution, Alumni Gala Ticket, Campus Mentorship Sponsorship
    @PostMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductRequestDto dto) {
        Product product = masterDataService.createProduct(dto);
        return new ResponseEntity<>(ApiResponse.ok("Product created successfully", product), HttpStatus.CREATED);
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(
            @RequestParam(required = false) ProductType type) {
        List<Product> products = (type != null) ? masterDataService.getProductsByType(type) : masterDataService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved products", products));
    }
}
