package com.alumni.portal.dto.response;

import com.alumni.portal.enums.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipSessionResponseDto {
    private Long id;
    private Long mentorId;
    private String mentorName;
    private String mentorExpertise;
    private String menteeName;
    private String menteeEmail;
    private String menteeDepartment;
    private String menteeBatch;
    private LocalDateTime sessionDate;
    private String topic;
    private SessionStatus status;
    private String notes;
    private LocalDateTime createdAt;
}
