package com.alumni.portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Journal is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "journal_id", nullable = false)
    private Journal journal;

    @NotNull(message = "Debit account is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debit_account_id", nullable = false)
    private ChartOfAccounts debitAccount;

    @NotNull(message = "Credit account is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_account_id", nullable = false)
    private ChartOfAccounts creditAccount;

    @NotNull(message = "Amount is required")
    @Column(nullable = false)
    private BigDecimal amount;

    @Builder.Default
    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate = LocalDateTime.now();

    @Column
    private String description;

    @Column(name = "reference_id")
    private String referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
}
