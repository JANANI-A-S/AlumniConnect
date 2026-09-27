package com.alumni.portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_registrations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"event_id", "alumni_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Event is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private AlumniEvent event;

    @NotNull(message = "Alumni is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alumni_id", nullable = false)
    private Alumni alumni;

    @Builder.Default
    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "attendance_status", nullable = false)
    private String attendanceStatus = "REGISTERED";

    @Column
    private String notes;
}
