package com.alumni.portal.service;

import com.alumni.portal.dto.request.ContactRequestDto;
import com.alumni.portal.dto.request.ProductRequestDto;
import com.alumni.portal.entity.Contact;
import com.alumni.portal.entity.Product;
import com.alumni.portal.enums.ContactType;
import com.alumni.portal.enums.ProductType;
import com.alumni.portal.exception.BadRequestException;
import com.alumni.portal.exception.ResourceNotFoundException;
import com.alumni.portal.repository.ContactRepository;
import com.alumni.portal.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterDataService {

    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Contact createContact(ContactRequestDto dto) {
        Contact contact = Contact.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .organization(dto.getOrganization())
                .contactType(dto.getContactType())
                .build();
        return contactRepository.save(contact);
    }

    @Transactional(readOnly = true)
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Contact> getContactsByType(ContactType type) {
        return contactRepository.findByContactType(type);
    }

    @Transactional
    public Product createProduct(ProductRequestDto dto) {
        if (productRepository.findByName(dto.getName()).isPresent()) {
            throw new BadRequestException("Product with name '" + dto.getName() + "' already exists.");
        }
        Product product = Product.builder()
                .name(dto.getName())
                .productType(dto.getProductType())
                .price(dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO)
                .description(dto.getDescription())
                .build();
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByType(ProductType type) {
        return productRepository.findByProductType(type);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }
}
