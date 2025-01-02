package com.dut.doctorcare.repositories;

import com.dut.doctorcare.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByDoctorId(UUID doctorId);
//    List<Appointment> findByPatientId(Long patientId);
//    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
//    List<Appointment> findByStatus(Appointment.Status status);
}
