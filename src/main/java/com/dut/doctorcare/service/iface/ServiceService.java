package com.dut.doctorcare.service.iface;

import com.dut.doctorcare.dto.request.ServiceDto;
import com.dut.doctorcare.dto.request.SpecializationDto;
import com.dut.doctorcare.dto.response.ServiceDoctorResponse;
import com.dut.doctorcare.model.Service;
import com.dut.doctorcare.model.Specialization;

import java.util.List;
import java.util.UUID;

public interface ServiceService {
    ServiceDto createService(ServiceDto request);
    List<ServiceDto> getAllServices();
    Service getServiceById(UUID id);
    ServiceDto updateService(UUID id, ServiceDto request);
    void deleteService(UUID id);
    List<ServiceDoctorResponse> getDoctorsByServiceSlug(String slug);

}
