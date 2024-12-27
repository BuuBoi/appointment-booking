package com.dut.doctorcare.controller;

import com.dut.doctorcare.dto.request.SpecializationDto;
import com.dut.doctorcare.dto.response.ApiResponse;
import com.dut.doctorcare.model.Specialization;
import com.dut.doctorcare.service.iface.SpecializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    private final SpecializationService specializationService;

    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping
    public ResponseEntity<SpecializationDto> createSpecialization(@RequestBody SpecializationDto request) {
        log.info("Creating specialization request: {}", request);
        SpecializationDto created = specializationService.createSpecialization(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<SpecializationDto>> getAllSpecializations() {
        List<SpecializationDto> specializations = specializationService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialization> getSpecializationById(@PathVariable String id) {
        Specialization specialization = specializationService.getSpecializationById(UUID.fromString(id));
        return ResponseEntity.ok(specialization);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecializationDto> updateSpecialization(
            @PathVariable String id,
            @RequestBody SpecializationDto request) {
        UUID uuid = UUID.fromString(id);
        SpecializationDto updated = specializationService.updateSpecialization(uuid, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable String id) {

        specializationService.deleteSpecialization(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}

