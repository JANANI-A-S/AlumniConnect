package com.alumni.portal.entity;

import com.alumni.portal.enums.BillStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bill Number is required")
    @Column(name = "bill_number", nullable = false, unique = true)
    private String billNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "po_id")
    private PurchaseOrder purchaseOrder;

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
    @Column(name = "bill_date", nullable = false)
    private LocalDate billDate = LocalDate.now();

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private BillStatus status = BillStatus.UNPAID;

    @Column
    private String notes;

    @Builder.Default
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
