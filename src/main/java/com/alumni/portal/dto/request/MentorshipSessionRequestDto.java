package com.alumni.portal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipSessionRequestDto {

    @NotNull(message = "Mentor ID is required")
    private Long mentorId;

    @NotBlank(message = "Mentee name is required")
    private String menteeName;

    private String menteeEmail;
    private String menteeDepartment;
    private String menteeBatch;

    @NotNull(message = "Session date & time is required")
    private LocalDateTime sessionDate;

    @NotBlank(message = "Session topic is required")
    private String topic;

    private String notes;
}
