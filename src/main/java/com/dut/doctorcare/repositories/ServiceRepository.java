package com.dut.doctorcare.repositories;

import com.dut.doctorcare.model.Doctor;
import com.dut.doctorcare.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    Optional<Service> findBySlug(String slug);
}
