package com.alumni.portal.dto.response;

import com.alumni.portal.enums.EventStatus;
import com.alumni.portal.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumniEventResponseDto {
    private Long id;
    private String name;
    private EventType eventType;
    private LocalDate eventDate;
    private String eventTime;
    private String venue;
    private String description;
    private Integer expectedAttendance;
    private Integer registeredCount;
    private BigDecimal eventBudget;
    private String eventBudgetFormatted;
    private BigDecimal actualExpense;
    private String actualExpenseFormatted;
    private BigDecimal sponsorshipAmount;
    private String sponsorshipAmountFormatted;
    private BigDecimal netVariance;
    private String netVarianceFormatted;
    private EventStatus status;
    private LocalDateTime createdAt;
}
