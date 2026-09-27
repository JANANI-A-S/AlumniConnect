package com.alumni.portal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorRequestDto {

    @NotNull(message = "Alumni ID is required")
    private Long alumniId;

    private String organization;
    private String designation;

    @NotBlank(message = "Expertise areas are required")
    private String expertise;

    private Integer yearsOfExperience;
    private String preferredMentorshipAreas;
    private String communicationMethod;
}
