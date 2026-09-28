package com.alumni.portal.entity;

import com.alumni.portal.enums.SessionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentorship_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Mentor is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", nullable = false)
    private MentorProfile mentor;

    @NotBlank(message = "Mentee name is required")
    @Column(name = "mentee_name", nullable = false)
    private String menteeName;

    @Column(name = "mentee_email")
    private String menteeEmail;

    @Column(name = "mentee_department")
    private String menteeDepartment;

    @Column(name = "mentee_batch")
    private String menteeBatch;

    @NotNull(message = "Session date & time is required")
    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;

    @NotBlank(message = "Session topic is required")
    @Column(nullable = false)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.SCHEDULED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
