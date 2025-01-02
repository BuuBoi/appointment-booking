package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dto.request.AppointmentDto;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.mapper.AppointmentMapper;
import com.dut.doctorcare.model.Appointment;
import com.dut.doctorcare.repositories.AppointmentRepository;
import com.dut.doctorcare.repositories.DoctorRepository;
import com.dut.doctorcare.repositories.PatientRepository;
import com.dut.doctorcare.repositories.UserRepository;
import com.dut.doctorcare.service.iface.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    public final PatientRepository patientRepository;


    @Override
    public AppointmentDto createAppointment(AppointmentDto request) {
        Appointment appointment = appointmentMapper.toEntity(request);
        appointment.setDoctor(doctorRepository.findById(UUID.fromString(request.getDoctorId())).orElseThrow());
        if(request.getUserId()!= null){
            appointment.setUser(userRepository.findById(UUID.fromString(request.getUserId())).orElse(null));
        }
        if(request.getPatientId()!= null){
            appointment.setPatient(patientRepository.findById(UUID.fromString(request.getPatientId())).orElse(null));
        }
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto getAppointmentById(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(UUID.fromString(appointmentId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return appointmentMapper.toDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAppointmentByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctorId(UUID.fromString(doctorId)).stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto updateAppointment(String appointmentId, AppointmentDto request) {
        Appointment existingAppointment = appointmentRepository.findById(UUID.fromString(appointmentId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Appointment updatedAppointment = appointmentMapper.toEntity(request);
        updatedAppointment.setId(existingAppointment.getId());
        updatedAppointment = appointmentRepository.save(updatedAppointment);

        return appointmentMapper.toDto(updatedAppointment);
    }

    @Override
    public void deleteAppointment(String appointmentId) {
        if (!appointmentRepository.existsById(UUID.fromString(appointmentId))) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        appointmentRepository.deleteById(UUID.fromString(appointmentId));
    }
}
