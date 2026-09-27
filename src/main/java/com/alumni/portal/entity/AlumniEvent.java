package com.alumni.portal.entity;

import com.alumni.portal.enums.EventStatus;
import com.alumni.portal.enums.EventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "alumni_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumniEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Event type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @NotNull(message = "Event date is required")
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_time")
    private String eventTime;

    @NotBlank(message = "Venue is required")
    @Column(nullable = false)
    private String venue;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    @Column(name = "expected_attendance")
    private Integer expectedAttendance = 100;

    @Builder.Default
    @Column(name = "registered_count")
    private Integer registeredCount = 0;

    @Builder.Default
    @Column(name = "event_budget", nullable = false)
    private BigDecimal eventBudget = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "actual_expense", nullable = false)
    private BigDecimal actualExpense = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "sponsorship_amount", nullable = false)
    private BigDecimal sponsorshipAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private EventStatus status = EventStatus.UPCOMING;

    @Builder.Default
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
