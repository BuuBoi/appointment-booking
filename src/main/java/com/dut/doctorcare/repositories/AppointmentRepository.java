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
    List<Appointment> findByPatientId(UUID patientId);
    @Query(value = """
    SELECT DISTINCT BIN_TO_UUID(p.id) AS id, p.email, p.occupation, p.full_name, p.phone_number, p.gender, p.avatar, p.date_of_birth, p.address FROM patients p
    INNER JOIN appointments a ON p.id = a.patient_id
    WHERE a.doctor_id = :doctorId AND a.status = :status
    """, nativeQuery = true)
    List<Object[]> findDistinctPatientsByDoctorAndStatus(
            @Param("doctorId") UUID doctorId,
            @Param("status") String status
    );
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.patient.id = :patientId")
    List<Appointment> findAllByDoctorIdAndPatientId(@Param("doctorId") UUID doctorId, @Param("patientId") UUID patientId);
//    List<Appointment> findByPatientId(Long patientId);
//    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
//    List<Appointment> findByStatus(Appointment.Status status);
}
