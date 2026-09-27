package com.alumni.portal.dto.request;

import com.alumni.portal.enums.EventStatus;
import com.alumni.portal.enums.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumniEventRequestDto {

    @NotBlank(message = "Event name is required")
    private String name;

    @NotNull(message = "Event type is required")
    private EventType eventType;

    @NotNull(message = "Event date is required")
    private LocalDate eventDate;

    private String eventTime;

    @NotBlank(message = "Venue is required")
    private String venue;

    private String description;
    private Integer expectedAttendance;
    private BigDecimal eventBudget;
    private BigDecimal sponsorshipAmount;
    private EventStatus status;
}
