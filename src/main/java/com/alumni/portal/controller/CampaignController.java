package com.alumni.portal.controller;

import com.alumni.portal.dto.request.CampaignRequestDto;
import com.alumni.portal.dto.response.ApiResponse;
import com.alumni.portal.dto.response.CampaignResponseDto;
import com.alumni.portal.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<CampaignResponseDto>> createCampaign(@Valid @RequestBody CampaignRequestDto dto) {
        CampaignResponseDto campaign = campaignService.createCampaign(dto);
        return new ResponseEntity<>(ApiResponse.ok("Campaign created successfully", campaign), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CampaignResponseDto>>> getAllCampaigns() {
        List<CampaignResponseDto> list = campaignService.getAllCampaigns();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved all campaigns", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CampaignResponseDto>> getCampaignById(@PathVariable Long id) {
        CampaignResponseDto campaign = campaignService.getCampaignById(id);
        return ResponseEntity.ok(ApiResponse.ok("Retrieved campaign details", campaign));
    }
}
