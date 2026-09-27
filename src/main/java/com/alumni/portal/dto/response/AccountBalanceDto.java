package com.alumni.portal.dto.response;

import com.alumni.portal.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceDto {
    private Long id;
    private String code;
    private String name;
    private AccountType type;
    private BigDecimal balance;
}
