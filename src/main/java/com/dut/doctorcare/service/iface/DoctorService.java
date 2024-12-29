package com.dut.doctorcare.service.iface;

import com.dut.doctorcare.dto.request.DoctorCreateDTO;
import com.dut.doctorcare.dto.request.DoctorRequest;
import com.dut.doctorcare.dto.request.PatientRequest;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.dto.response.PatientResponse;
import com.dut.doctorcare.model.Doctor;

public interface DoctorService {
    DoctorResponse saveOrUpdate(DoctorRequest request);
    DoctorResponse getMyInfo();
    Doctor createDoctor(DoctorCreateDTO request);
}
