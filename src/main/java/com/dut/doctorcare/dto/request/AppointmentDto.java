package com.dut.doctorcare.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Getter
@Setter
public class AppointmentDto {

        private String id;
        private String status;
        private Double fee;
        private String doctorId;
        private String patientId;
        private String userId;
        private LocalDate appointmentDate;
        private String appointmentTime;
        private String address;
        private LocalDateTime appointmentCreatedDate;
        private LocalDate dob;
        private String email;
        private String fullName;
        private String gender;
        private String medicalDocument;
        private String occupation;
        private String phone;
        private String reason;
    }

