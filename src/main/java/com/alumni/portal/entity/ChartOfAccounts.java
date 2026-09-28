package com.alumni.portal.entity;

import com.alumni.portal.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "chart_of_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChartOfAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account code is required")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Account name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Account type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column
    private String description;
}
