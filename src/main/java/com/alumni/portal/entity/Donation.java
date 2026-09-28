package com.alumni.portal.entity;

import com.alumni.portal.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Alumni is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alumni_id", nullable = false)
    private Alumni alumni;

    @NotNull(message = "Campaign is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull(message = "Donation amount is required")
    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "donation_date", nullable = false)
    private LocalDateTime donationDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;

    @Column(name = "receipt_number", nullable = false, unique = true)
    private String receiptNumber;

    @Column
    private String notes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;
}
