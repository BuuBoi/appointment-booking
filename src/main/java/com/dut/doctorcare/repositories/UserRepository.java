package com.dut.doctorcare.repositories;

import com.dut.doctorcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Sử dụng ID kiểu String (UUID)
    Optional<User> findByEmail(String email);
}
