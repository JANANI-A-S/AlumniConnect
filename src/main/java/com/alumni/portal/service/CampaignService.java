package com.alumni.portal.service;

import com.alumni.portal.dto.request.CampaignRequestDto;
import com.alumni.portal.dto.response.CampaignResponseDto;
import com.alumni.portal.entity.Budget;
import com.alumni.portal.entity.Campaign;
import com.alumni.portal.exception.BadRequestException;
import com.alumni.portal.exception.ResourceNotFoundException;
import com.alumni.portal.repository.BudgetRepository;
import com.alumni.portal.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final BudgetRepository budgetRepository;

    @Transactional
    public CampaignResponseDto createCampaign(CampaignRequestDto dto) {
        if (campaignRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Campaign with name '" + dto.getName() + "' already exists.");
        }

        Campaign campaign = Campaign.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .targetAmount(dto.getTargetAmount())
                .collectedAmount(BigDecimal.ZERO)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .build();

        Campaign saved = campaignRepository.save(campaign);

        // Auto-create initial budget tracking record for analytic accounting
        Budget budget = Budget.builder()
                .campaign(saved)
                .targetAmount(saved.getTargetAmount())
                .actualAmount(BigDecimal.ZERO)
                .allocatedExpenseBudget(saved.getTargetAmount().multiply(new BigDecimal("0.15"))) // 15% estimated budget for marketing/gala
                .actualExpenses(BigDecimal.ZERO)
                .notes("Auto-initialized budget for campaign: " + saved.getName())
                .build();
        budgetRepository.save(budget);

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CampaignResponseDto> getAllCampaigns() {
        return campaignRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CampaignResponseDto getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + id));
        return toDto(campaign);
    }

    public CampaignResponseDto toDto(Campaign c) {
        BigDecimal remaining = c.getTargetAmount().subtract(c.getCollectedAmount());
        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            remaining = BigDecimal.ZERO;
        }

        double percentage = 0.0;
        if (c.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
            percentage = c.getCollectedAmount()
                    .divide(c.getTargetAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .doubleValue();
        }

        return CampaignResponseDto.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .targetAmount(c.getTargetAmount())
                .collectedAmount(c.getCollectedAmount())
                .remainingAmount(remaining)
                .percentageAchieved(percentage)
                .status(c.getStatus())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .build();
    }
}
