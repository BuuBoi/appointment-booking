package com.dut.doctorcare.repositories;

import com.dut.doctorcare.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, UUID> {
    Optional<Specialization> findBySlug(String slug);
}

