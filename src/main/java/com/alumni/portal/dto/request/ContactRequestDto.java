package com.alumni.portal.dto.request;

import com.alumni.portal.enums.ContactType;
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
public class ContactRequestDto {

    @NotBlank(message = "Contact name is required")
    private String name;

    private String email;
    private String phone;
    private String organization;

    @NotNull(message = "Contact type is required (DONOR, VENDOR, AGENCY)")
    private ContactType contactType;
}
