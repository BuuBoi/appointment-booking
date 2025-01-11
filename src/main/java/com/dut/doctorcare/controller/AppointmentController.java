package com.dut.doctorcare.controller;

import com.dut.doctorcare.dto.request.AppointmentDto;
import com.dut.doctorcare.service.iface.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDTO) {
        AppointmentDto created = appointmentService.createAppointment(appointmentDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable String id) {
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable String id,
            @RequestBody AppointmentDto appointmentDTO) {
        AppointmentDto updated = appointmentService.updateAppointment(id, appointmentDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDoctor(@PathVariable String doctorId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatient(@PathVariable String patientId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentByPatientId(patientId);
        return ResponseEntity.ok(appointments);
    }
    @GetMapping("/doctor/{doctorId}/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDoctorAndPatient(@PathVariable String doctorId, @PathVariable String patientId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByDoctorAndPatient(UUID.fromString(doctorId), UUID.fromString(patientId));
        return ResponseEntity.ok(appointments);
    }
}
