package com.alumni.portal.entity;

import com.alumni.portal.enums.ContactType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Contact / Vendor name is required")
    @Column(nullable = false)
    private String name;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String organization;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "bank_details")
    private String bankDetails;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @NotNull(message = "Contact type is required (DONOR, VENDOR, AGENCY)")
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false)
    private ContactType contactType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
