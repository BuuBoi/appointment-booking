package com.dut.doctorcare.service.iface;

import com.dut.doctorcare.dto.request.SpecializationDto;
import com.dut.doctorcare.dto.response.ServiceDoctorResponse;
import com.dut.doctorcare.model.Role;
import com.dut.doctorcare.model.Specialization;

import java.util.List;
import java.util.UUID;

public interface SpecializationService {
    SpecializationDto createSpecialization(SpecializationDto request);
    List<SpecializationDto> getAllSpecializations();
    Specialization getSpecializationById(UUID id);
    SpecializationDto updateSpecialization(UUID id, SpecializationDto specialization);
    void deleteSpecialization(UUID id);
    List<ServiceDoctorResponse> getDoctorsBySpecialSlug(String slug);
}
