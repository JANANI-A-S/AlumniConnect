package com.alumni.portal.dto.request;

import com.alumni.portal.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartOfAccountsRequestDto {

    @NotBlank(message = "Account code is required")
    private String code;

    @NotBlank(message = "Account name is required")
    private String name;

    @NotNull(message = "Account type is required (ASSET, LIABILITY, INCOME, EXPENSE)")
    private AccountType type;

    private String description;
}
