package com.dut.doctorcare.controller;

import com.dut.doctorcare.dto.request.*;
import com.dut.doctorcare.dto.response.ApiResponse;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.dto.response.PatientResponse;
import com.dut.doctorcare.dto.response.UserResponseDto;
import com.dut.doctorcare.mapper.DoctorMapper;
import com.dut.doctorcare.model.Doctor;
import com.dut.doctorcare.model.Service;
import com.dut.doctorcare.service.iface.DoctorService;
import com.dut.doctorcare.service.iface.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @PostMapping
    @RequestMapping("/profile")
    public ApiResponse<DoctorResponse> saveOrUpdate(@RequestBody DoctorRequest request) {
        DoctorResponse doctorResponse = doctorService.saveOrUpdate(request);
        return ApiResponse.<DoctorResponse>builder()
                .status(200)
                .data(doctorResponse)
                .build();
    }
    @GetMapping("/profile")
    public ApiResponse<DoctorResponse> getMyInfo(){
        ApiResponse<DoctorResponse> response = new ApiResponse<>();
        response.setStatus(200);
        response.setData(doctorService.getMyInfo());
        return response;
    }

    @GetMapping
    public ApiResponse<List<DoctorResponse>> getAllDoctors() {
        List<DoctorResponse> doctorResponses = doctorService.getAllDoctors();
        return ApiResponse.<List<DoctorResponse>>builder()
                .status(200)
                .data(doctorResponses)
                .build();
    }
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody DoctorCreateDTO doctorCreateDTO) {
        Doctor createdDoctor = doctorService.createDoctor(doctorCreateDTO);
        return ResponseEntity.ok(createdDoctor);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable String doctorId) {
        log.info("Getting doctor with id: {}", doctorId);
        DoctorResponse doctorResponse = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctorResponse);
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable String doctorId, @RequestBody Map<String, Object> fields) {
        log.info("Updating doctor with id: {}", doctorId);
        log.info("Fields: {}", fields);
        Doctor doctorResponse = doctorService.updateDoctor(doctorId, fields);
        DoctorResponse doctorResponse1 = doctorMapper.toDoctorResponse(doctorResponse);
        return ResponseEntity.ok(doctorResponse1);
    }

    @GetMapping("/{doctorId}/service")
    public ResponseEntity<ServiceDto> getServiceByDoctorId(@PathVariable String doctorId) {
        ServiceDto serviceDto = doctorService.getServiceByDoctorId(doctorId);
        return ResponseEntity.ok(serviceDto);
    }

    @GetMapping("/{doctorId}/specialization")
    public ResponseEntity<SpecializationDto> getSpecializationByDoctorId(@PathVariable String doctorId) {
        SpecializationDto serviceDto = doctorService.getSpecializationByDoctorId(doctorId);
        return ResponseEntity.ok(serviceDto);
    }

    // API để set service cho bác sĩ
    @PostMapping("/{doctorId}/service/{serviceId}")
    public ResponseEntity<DoctorResponse> setServiceForDoctor(
            @PathVariable String doctorId,
            @PathVariable String serviceId) {
        Doctor doctor = doctorService.setServiceForDoctor(doctorId, serviceId);
        Service service = doctor.getService();
        DoctorResponse doctorResponse = doctorMapper.toDoctorResponse(doctor);
        return ResponseEntity.ok(doctorResponse);
    }

    // API để set service cho bác sĩ
    @PostMapping("/{doctorId}/specialization/{specialId}")
    public ResponseEntity<DoctorResponse> setSpecializationForDoctor(
            @PathVariable String doctorId,
            @PathVariable String specialId) {
        Doctor doctor = doctorService.setSpecializationForDoctor(doctorId, specialId);
        DoctorResponse doctorResponse = doctorMapper.toDoctorResponse(doctor);
        return ResponseEntity.ok(doctorResponse);
    }

    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<List<PatientResponse>> getMyPatient(@PathVariable String doctorId) {
        log.info("Getting my patients:{}", doctorId);
        List<PatientResponse> patientResponses = doctorService.getMyPatient(doctorId);
        return ResponseEntity.ok(patientResponses);
    }






























//    @PutMapping("/{patientId}")
//    public ApiResponse<PatientResponse> updatePatient(@PathVariable String patientId, @RequestBody PatientRequest patientRequest) {
//        PatientResponse patientResponse = patientService.saveOrUpdate(patientId, patientRequest);
//        return ApiResponse.<PatientResponse>builder()
//                .status(200)
//                .data(patientResponse)
//                .build();
//    }
//
//    @GetMapping("/{patientId}")
//    public ApiResponse<PatientResponse> getPatient(@PathVariable String patientId) {
//        PatientResponse patientResponse = patientService.getPatientById(patientId);
//        return ApiResponse.<PatientResponse>builder()
//                .status(200)
//                .data(patientResponse)
//                .build();
//    }
//
//    @GetMapping
//    public ApiResponse<PatientResponse> getPatients() {
//        PatientResponse patientResponse = patientService.getPatients();
//        return ApiResponse.<PatientResponse>builder()
//                .status(200)
//                .data(patientResponse)
//                .build();
//    }
}
