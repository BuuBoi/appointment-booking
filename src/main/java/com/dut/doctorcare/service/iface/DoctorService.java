package com.dut.doctorcare.service.iface;

import com.dut.doctorcare.dto.request.*;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.dto.response.PatientResponse;
import com.dut.doctorcare.model.Doctor;

import java.util.List;
import java.util.Map;

public interface DoctorService {
    DoctorResponse saveOrUpdate(DoctorRequest request);
    List<DoctorResponse> getAllDoctors();
    DoctorResponse getDoctorById(String doctorId);
    DoctorResponse getMyInfo();
    Doctor createDoctor(DoctorCreateDTO request);
    Doctor updateDoctor(String doctorId, Map<String, Object> fields);
    ServiceDto getServiceByDoctorId (String doctorId);
    Doctor setServiceForDoctor(String doctorId, String serviceId);
    SpecializationDto getSpecializationByDoctorId(String doctorId);
    Doctor setSpecializationForDoctor(String doctorId, String serviceId);
    List<PatientResponse> getMyPatient(String doctorId);
    List<DoctorResponse> getDoctorBySpecialization(String specialSlug);
}
