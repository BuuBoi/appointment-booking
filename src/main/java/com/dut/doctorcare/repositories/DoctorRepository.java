package com.dut.doctorcare.repositories;

import com.dut.doctorcare.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    // Truy vấn tất cả bác sĩ sắp xếp theo ngày gần nhất (updatedAt giảm dần)
    List<Doctor> findAllByOrderByCreatedAtDesc();
}