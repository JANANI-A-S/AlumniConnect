package com.alumni.portal.entity;

import com.alumni.portal.enums.JournalType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "journals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Journal code is required")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Journal name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Journal type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JournalType type;

    @Column
    private String description;
}
