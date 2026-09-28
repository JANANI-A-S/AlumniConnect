package com.alumni.portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Alumni reference is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alumni_id", nullable = false)
    private Alumni alumni;

    @Column(name = "organization")
    private String organization;

    @Column(name = "designation")
    private String designation;

    @NotBlank(message = "Expertise areas are required")
    @Column(nullable = false)
    private String expertise;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience = 5;

    @Column(name = "preferred_mentorship_areas")
    private String preferredMentorshipAreas;

    @Column(name = "availability_status", nullable = false)
    private String availabilityStatus = "AVAILABLE";

    @Column(name = "communication_method")
    private String communicationMethod = "Google Meet / Video Call";

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
