package com.alumni.portal.dto.request;

import com.alumni.portal.enums.AlumniStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumniRequestDto {

    @NotBlank(message = "Alumni name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;

    @NotBlank(message = "Graduation batch / class year is required")
    private String batch;

    private String contact;
    private String department;
    private String currentOrganization;
    private String currentDesignation;
    private String cityLocation;
    private String linkedInProfile;
    private AlumniStatus alumniStatus;
    private Boolean mentorshipAvailable;
}
