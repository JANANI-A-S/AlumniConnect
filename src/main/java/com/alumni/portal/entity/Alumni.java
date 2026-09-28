package com.alumni.portal.entity;

import com.alumni.portal.enums.AlumniStatus;
import com.alumni.portal.enums.RankingTier;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "alumni")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alumni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Alumni name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Graduation batch/class year is required")
    @Column(nullable = false)
    private String batch;

    @Column(name = "contact_number")
    private String contact;

    @Column(name = "department")
    private String department;

    @Column(name = "current_organization")
    private String currentOrganization;

    @Column(name = "current_designation")
    private String currentDesignation;

    @Column(name = "city_location")
    private String cityLocation;

    @Column(name = "linkedin_profile")
    private String linkedInProfile;

    @Enumerated(EnumType.STRING)
    @Column(name = "alumni_status", nullable = false)
    private AlumniStatus alumniStatus = AlumniStatus.ACTIVE;

    @Column(name = "mentorship_available", nullable = false)
    private Boolean mentorshipAvailable = true;

    @Column(name = "donation_total", nullable = false)
    private BigDecimal donationTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "ranking_tier", nullable = false)
    private RankingTier rankingTier = RankingTier.BRONZE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public void recalculateTier() {
        this.rankingTier = RankingTier.calculateTier(this.donationTotal);
    }
}
