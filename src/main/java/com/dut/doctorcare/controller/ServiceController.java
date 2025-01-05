package com.dut.doctorcare.controller;

import com.dut.doctorcare.dto.request.ServiceDto;
import com.dut.doctorcare.dto.response.ApiResponse;
import com.dut.doctorcare.dto.response.ServiceDoctorResponse;
import com.dut.doctorcare.model.Service;
import com.dut.doctorcare.service.iface.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService ServiceService;

    public ServiceController(ServiceService ServiceService) {
        this.ServiceService = ServiceService;
    }

    @PostMapping
    public ResponseEntity<ServiceDto> createService(@RequestBody ServiceDto request) {
        log.info("Creating Service request: {}", request);
        ServiceDto created = ServiceService.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        List<ServiceDto> Services = ServiceService.getAllServices();
        return ResponseEntity.ok(Services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable String id) {
        Service Service = ServiceService.getServiceById(UUID.fromString(id));
        return ResponseEntity.ok(Service);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto> updateService(
            @PathVariable String id,
            @RequestBody ServiceDto request) {
        UUID uuid = UUID.fromString(id);
        ServiceDto updated = ServiceService.updateService(uuid, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable String id) {

        ServiceService.deleteService(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{slug}/doctors")
    public ResponseEntity<List<ServiceDoctorResponse>> getDoctorsByService(@PathVariable String slug) {
        return ResponseEntity.ok(ServiceService.getDoctorsByServiceSlug(slug));
    }
}

