package com.dut.doctorcare.controller;

import com.dut.doctorcare.dto.request.DoctorCreateDTO;
import com.dut.doctorcare.dto.request.DoctorRequest;
import com.dut.doctorcare.dto.request.PatientRequest;
import com.dut.doctorcare.dto.response.ApiResponse;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.dto.response.PatientResponse;
import com.dut.doctorcare.mapper.DoctorMapper;
import com.dut.doctorcare.model.Doctor;
import com.dut.doctorcare.service.iface.DoctorService;
import com.dut.doctorcare.service.iface.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
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
        DoctorResponse doctorResponse = doctorService.getMyInfo();
        return ApiResponse.<DoctorResponse>builder()
                .status(200)
                .data(doctorResponse)
                .build();
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody DoctorCreateDTO doctorCreateDTO) {
        Doctor createdDoctor = doctorService.createDoctor(doctorCreateDTO);
        return ResponseEntity.ok(createdDoctor);
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable String doctorId, @RequestBody Map<String, Object> fields) {
        log.info("Updating doctor with id: {}", doctorId);
        log.info("Fields: {}", fields);
        Doctor doctorResponse = doctorService.updateDoctor(doctorId, fields);
        DoctorResponse doctorResponse1 = doctorMapper.toDoctorResponse(doctorResponse);
        return ResponseEntity.ok(doctorResponse1);
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
