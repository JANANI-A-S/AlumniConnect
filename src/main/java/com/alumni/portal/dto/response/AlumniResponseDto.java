package com.alumni.portal.dto.response;

import com.alumni.portal.enums.AlumniStatus;
import com.alumni.portal.enums.RankingTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumniResponseDto {
    private Long id;
    private String name;
    private String email;
    private String batch;
    private String contact;
    private String department;
    private String currentOrganization;
    private String currentDesignation;
    private String cityLocation;
    private String linkedInProfile;
    private AlumniStatus alumniStatus;
    private Boolean mentorshipAvailable;
    private BigDecimal donationTotal;
    private String donationTotalFormatted;
    private RankingTier rankingTier;
    private LocalDateTime createdAt;
}
