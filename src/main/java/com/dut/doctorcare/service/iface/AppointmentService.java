package com.dut.doctorcare.service.iface;

import com.dut.doctorcare.dto.request.AppointmentDto;
import com.dut.doctorcare.dto.response.AppointmentResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto request);
    List<AppointmentDto> getAllAppointments();
    AppointmentDto getAppointmentById(String appointmentId);
    List<AppointmentDto> getAppointmentByDoctorId(String doctorId);
    AppointmentDto updateAppointment(String appointmentId, AppointmentDto request);
    void deleteAppointment(String appointmentId);
}
