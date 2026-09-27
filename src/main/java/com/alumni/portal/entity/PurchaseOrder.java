package com.alumni.portal.entity;

import com.alumni.portal.enums.POStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "PO Number is required")
    @Column(name = "po_number", nullable = false, unique = true)
    private String poNumber;

    @NotNull(message = "Vendor is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Contact vendor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private AlumniEvent event;

    @NotNull(message = "Amount is required")
    @Column(nullable = false)
    private BigDecimal amount;

    @Builder.Default
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private POStatus status = POStatus.APPROVED;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
