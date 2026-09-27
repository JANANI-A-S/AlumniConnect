package com.alumni.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorResponseDto {
    private Long id;
    private Long alumniId;
    private String alumniName;
    private String alumniEmail;
    private String batch;
    private String department;
    private String organization;
    private String designation;
    private String expertise;
    private Integer yearsOfExperience;
    private String preferredMentorshipAreas;
    private String availabilityStatus;
    private String communicationMethod;
    private Boolean active;
    private long totalSessionsCount;
    private LocalDateTime createdAt;
}
