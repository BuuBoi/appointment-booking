package com.dut.doctorcare.controller;

import com.dut.doctorcare.dto.request.PatientRequest;
import com.dut.doctorcare.dto.response.ApiResponse;
import com.dut.doctorcare.dto.response.PatientResponse;
import com.dut.doctorcare.exception.EntityOperationException;
import com.dut.doctorcare.service.iface.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping("/profile")
    public ApiResponse<PatientResponse> addPatientProfile(@RequestBody PatientRequest patientRequest) {
        PatientResponse patientResponse = patientService.saveOrUpdate(patientRequest);
        return ApiResponse.<PatientResponse>builder()
                .status(200)
                .data(patientResponse)
                .build();
    }
    @GetMapping("/profile")
    public ApiResponse<PatientResponse> getMyInfo(){
        PatientResponse patientResponse = patientService.getMyInfo();
        return ApiResponse.<PatientResponse>builder()
                .status(200)
                .data(patientResponse)
                .build();
    }

    @GetMapping()
    public ApiResponse<List<PatientResponse>> getPatients() {
        List<PatientResponse> patientResponses = patientService.getPatients();
        return ApiResponse.<List<PatientResponse>>builder()
                .status(200)
                .data(patientResponses)
                .build();
    }
    @GetMapping("/{patientId}")
    public ApiResponse<PatientResponse> getPatient(@PathVariable String patientId) {
        PatientResponse patientResponse = patientService.getPatientById(patientId);
        return ApiResponse.<PatientResponse>builder()
                .status(200)
                .data(patientResponse)
                .build();
    }

    @DeleteMapping("/{patientId}")
    public ApiResponse<PatientResponse> deletePatient(@PathVariable String patientId) throws EntityOperationException {
        patientService.deletePatient(patientId);
        return ApiResponse.<PatientResponse>builder()
                .status(200)
                .message("Delete success")
                .build();
    }



}
