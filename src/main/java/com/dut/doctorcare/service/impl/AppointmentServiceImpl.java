package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dto.request.AppointmentDto;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.mapper.AppointmentMapper;
import com.dut.doctorcare.model.Appointment;
import com.dut.doctorcare.model.Patient;
import com.dut.doctorcare.repositories.AppointmentRepository;
import com.dut.doctorcare.repositories.DoctorRepository;
import com.dut.doctorcare.repositories.PatientRepository;
import com.dut.doctorcare.repositories.UserRepository;
import com.dut.doctorcare.service.iface.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
    @Autowired
    private EmailNotificationService emailNotificationService;


    @Override
    public AppointmentDto createAppointment(AppointmentDto request) {
        Appointment appointment = appointmentMapper.toEntity(request);
        appointment.setDoctor(doctorRepository.findById(UUID.fromString(request.getDoctorId())).orElseThrow());
        if (request.getUserId() != null) {
            appointment.setUser(userRepository.findById(UUID.fromString(request.getUserId())).orElse(null));
        }
        if (request.getPatientId() != null) {
            appointment.setPatient(patientRepository.findById(UUID.fromString(request.getPatientId())).orElse(null));
        }
        appointment = appointmentRepository.save(appointment);
        try {
            emailNotificationService.sendDoctorAppointmentNotification(appointment);
        } catch (Exception e) {
            log.error("Failed to send doctor notification email", e);
        }
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
    public List<AppointmentDto> getAppointmentByPatientId(String patientId) {
//        return appointmentRepository.findByUserId(UUID.fromString(patientId)).stream()
//                .map(appointmentMapper::toDto)
//                .collect(Collectors.toList());
        return appointmentRepository.findByPatientId(UUID.fromString(patientId)).stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<AppointmentDto> getAppointmentByUserId(String userId) {
        return appointmentRepository.findByUserId(UUID.fromString(userId)).stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
//        return appointmentRepository.findByPatientId(UUID.fromString(patientId)).stream()
//                .map(appointmentMapper::toDto)
//                .collect(Collectors.toList());
    }


    @Override
    public AppointmentDto updateAppointment(String appointmentId, AppointmentDto request) {
        Appointment existingAppointment = appointmentRepository.findById(UUID.fromString(appointmentId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //code nay khong chinh xac,vi update 2 truong thi khong can tao nguyen 1 doi tuong moi, thu hai la
        //trong mapper ta ignore cac truong doctor, patient, user nen updateAppointment bi null cac truong doctor
        //patient, user, vi vay trong csdl khong khong co id cua doctor truy van bi loi
        //ma cung khong the bo ignore trong mapper vi payload gui len co the thieu cac truuong nay nen bi loi mapStruct vs method get
        // bay gio chinh sua lai code o duoi
//        Appointment updatedAppointment = appointmentMapper.toEntity(request);
//        log.info(String.valueOf(existingAppointment.getId()));
//        updatedAppointment.setId(existingAppointment.getId());
//        updatedAppointment = appointmentRepository.save(updatedAppointment)
//code moi
        if (request.getStatus() != null) {
            existingAppointment.setStatus(request.getStatus());
        }

        if (request.getMeetingLink() != null) {
            existingAppointment.setMeetingLink(request.getMeetingLink());
        }

        final Appointment updatedAppointment = appointmentRepository.save(existingAppointment);

        if (Appointment.Status.ACCEPTED.equals(request.getStatus())) {
            try {
                // Kiểm tra patient dựa vào tên và số điện thoại
                Patient patient = patientRepository
                        .findByFullNameAndPhoneNumber(
                                existingAppointment.getFullName(),
                                existingAppointment.getPhone()
                        )
                        .orElseGet(() -> createNewPatient(updatedAppointment));

                patient = patientRepository.save(patient);
                updatedAppointment.setPatient(patient);
                appointmentRepository.save(updatedAppointment);

            } catch (Exception e) {
                log.error("Failed to create/update patient record", e);
//                throw new AppException(ErrorCode.PATIENT_CREATION_FAILED);
            }
        }

        try {
            emailNotificationService.sendPatientStatusNotification(updatedAppointment);
        } catch (Exception e) {
            log.error("Failed to send patient notification email", e);
        }
        return appointmentMapper.toDto(updatedAppointment);
    }

    @Override
    public void deleteAppointment(String appointmentId) {
        if (!appointmentRepository.existsById(UUID.fromString(appointmentId))) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        appointmentRepository.deleteById(UUID.fromString(appointmentId));
    }



    private Patient createNewPatient(Appointment appointment) {
        Patient patient = new Patient();

        patient.setFullName(appointment.getFullName());
        patient.setPhoneNumber(appointment.getPhone());
        patient.setEmail(appointment.getEmail());  // vẫn lưu email để liên hệ
        patient.setGender(Patient.Gender.valueOf(appointment.getGender()));
        patient.setDateOfBirth(appointment.getDob());
        patient.setAddress(appointment.getAddress());
        patient.setOccupation(appointment.getOccupation());

        return patientRepository.save(patient);
    }

    // Lấy danh sách các cuộc hẹn giữa bác sĩ và bệnh nhân
    public List<AppointmentDto> getAppointmentsByDoctorAndPatient(UUID doctorId, UUID patientId) {
        List<Appointment> appointment= appointmentRepository.findAllByDoctorIdAndPatientId(doctorId, patientId);
        List<AppointmentDto> appointmentDtos = appointment.stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
        return appointmentDtos;
    }
}
