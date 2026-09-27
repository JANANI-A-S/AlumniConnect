package com.alumni.portal.controller;

import com.alumni.portal.dto.request.AlumniRequestDto;
import com.alumni.portal.dto.response.ApiResponse;
import com.alumni.portal.dto.response.AlumniResponseDto;
import com.alumni.portal.service.AlumniService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumni")
@RequiredArgsConstructor
public class AlumniController {

    private final AlumniService alumniService;

    @PostMapping
    public ResponseEntity<ApiResponse<AlumniResponseDto>> registerAlumni(@Valid @RequestBody AlumniRequestDto dto) {
        AlumniResponseDto created = alumniService.registerAlumni(dto);
        return new ResponseEntity<>(ApiResponse.ok("Alumni registered successfully", created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AlumniResponseDto>>> getAllAlumni() {
        List<AlumniResponseDto> list = alumniService.getAllAlumni();
        return ResponseEntity.ok(ApiResponse.ok("Retrieved all alumni records", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlumniResponseDto>> getAlumniById(@PathVariable Long id) {
        AlumniResponseDto alumni = alumniService.getAlumniById(id);
        return ResponseEntity.ok(ApiResponse.ok("Retrieved alumni profile", alumni));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AlumniResponseDto>>> searchAndRankAlumni(
            @RequestParam(value = "query", required = false, defaultValue = "") String query) {
        List<AlumniResponseDto> results = alumniService.searchAndRankAlumni(query);
        return ResponseEntity.ok(ApiResponse.ok("Retrieved ranked alumni results", results));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVOICING_USER')")
    public ResponseEntity<ApiResponse<AlumniResponseDto>> updateAlumni(
            @PathVariable Long id,
            @Valid @RequestBody AlumniRequestDto dto) {
        AlumniResponseDto updated = alumniService.updateAlumni(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Alumni updated successfully", updated));
    }
}
