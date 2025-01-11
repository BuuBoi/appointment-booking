package com.dut.doctorcare.service.iface;

import com.dut.doctorcare.dto.request.AppointmentDto;
import com.dut.doctorcare.dto.response.AppointmentResponse;
import com.dut.doctorcare.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto request);
    List<AppointmentDto> getAllAppointments();
    AppointmentDto getAppointmentById(String appointmentId);
    List<AppointmentDto> getAppointmentByDoctorId(String doctorId);
    AppointmentDto updateAppointment(String appointmentId, AppointmentDto request);
    void deleteAppointment(String appointmentId);
    List<AppointmentDto>getAppointmentByPatientId(String patientId);
    List<AppointmentDto> getAppointmentsByDoctorAndPatient(UUID doctorId, UUID patientId);
}
