package com.dut.doctorcare.repositories;

import com.dut.doctorcare.model.Appointment;
import com.dut.doctorcare.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByDoctorId(UUID doctorId);
    List<Appointment> findByUserId(UUID userId);
    @Query(value = """
    SELECT DISTINCT p.id, p.email, p.occupation, p.full_name, p.phone_number, p.gender, p.avatar, p.date_of_birth, p.address FROM patients p
    INNER JOIN appointments a ON p.id = a.patient_id
    WHERE a.doctor_id = :doctorId AND a.status = :status
    """, nativeQuery = true)
    List<Object[]> findDistinctPatientsByDoctorAndStatus(
            @Param("doctorId") UUID doctorId,
            @Param("status") String status
    );
//    List<Appointment> findByPatientId(Long patientId);
//    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
//    List<Appointment> findByStatus(Appointment.Status status);
}
