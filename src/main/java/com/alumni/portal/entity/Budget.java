package com.alumni.portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Campaign is required")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campaign_id", nullable = false, unique = true)
    private Campaign campaign;

    @NotNull(message = "Target amount is required")
    @Column(name = "target_amount", nullable = false)
    private BigDecimal targetAmount;

    @Builder.Default
    @Column(name = "actual_amount", nullable = false)
    private BigDecimal actualAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "allocated_expense_budget", nullable = false)
    private BigDecimal allocatedExpenseBudget = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "actual_expenses", nullable = false)
    private BigDecimal actualExpenses = BigDecimal.ZERO;

    @Column
    private String notes;
}
