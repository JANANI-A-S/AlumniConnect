package com.alumni.portal.service;

import com.alumni.portal.dto.request.AlumniRequestDto;
import com.alumni.portal.dto.response.AlumniResponseDto;
import com.alumni.portal.entity.Alumni;
import com.alumni.portal.enums.RankingTier;
import com.alumni.portal.exception.BadRequestException;
import com.alumni.portal.exception.ResourceNotFoundException;
import com.alumni.portal.repository.AlumniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumniService {

    private final AlumniRepository alumniRepository;

    @Transactional
    public AlumniResponseDto registerAlumni(AlumniRequestDto dto) {
        if (alumniRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Alumni with email " + dto.getEmail() + " is already registered.");
        }

        Alumni alumni = new Alumni();
        alumni.setName(dto.getName());
        alumni.setEmail(dto.getEmail());
        alumni.setBatch(dto.getBatch());
        alumni.setContact(dto.getContact());
        alumni.setDonationTotal(BigDecimal.ZERO);
        alumni.setRankingTier(RankingTier.BRONZE);

        Alumni saved = alumniRepository.save(alumni);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<AlumniResponseDto> getAllAlumni() {
        return alumniRepository.findAllByOrderByDonationTotalDesc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlumniResponseDto getAlumniById(Long id) {
        Alumni alumni = alumniRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumni not found with ID: " + id));
        return toDto(alumni);
    }

    @Transactional(readOnly = true)
    public List<AlumniResponseDto> searchAndRankAlumni(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllAlumni();
        }
        return alumniRepository.searchAlumni(query.trim())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AlumniResponseDto updateAlumni(Long id, AlumniRequestDto dto) {
        Alumni alumni = alumniRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumni not found with ID: " + id));

        if (!alumni.getEmail().equalsIgnoreCase(dto.getEmail()) && alumniRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email " + dto.getEmail() + " is already in use by another alumni.");
        }

        alumni.setName(dto.getName());
        alumni.setEmail(dto.getEmail());
        alumni.setBatch(dto.getBatch());
        alumni.setContact(dto.getContact());

        Alumni updated = alumniRepository.save(alumni);
        return toDto(updated);
    }

    public AlumniResponseDto toDto(Alumni a) {
        return AlumniResponseDto.builder()
                .id(a.getId())
                .name(a.getName())
                .email(a.getEmail())
                .batch(a.getBatch())
                .contact(a.getContact())
                .donationTotal(a.getDonationTotal())
                .rankingTier(a.getRankingTier())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
