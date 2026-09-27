package com.alumni.portal.dto.request;

import com.alumni.portal.enums.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Product type is required (ENDOWMENT_CONTRIBUTION, GALA_TICKET, MENTORSHIP_SPONSORSHIP)")
    private ProductType productType;

    @DecimalMin(value = "0.00", message = "Price cannot be negative")
    private BigDecimal price;

    private String description;
}
