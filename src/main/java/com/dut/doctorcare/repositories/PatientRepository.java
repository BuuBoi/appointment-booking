package com.dut.doctorcare.repositories;

import com.dut.doctorcare.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    // Sử dụng ID kiểu String (UUID)
    Optional<Patient> findByFullNameAndPhoneNumber(String fullName, String phoneNumber);



}
