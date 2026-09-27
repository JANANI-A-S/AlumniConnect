package com.alumni.portal.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationRequestDto {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Alumni ID is required")
    private Long alumniId;

    private String notes;
}
