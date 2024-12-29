package com.dut.doctorcare.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorCreateDTO {
    private String id; // ID của User
    private String fullName;
    private String bio;
    private LocalDate dob; // Date of Birth
    private String gender;
    private String medicalLicense;
    private LocalDate medicalLicenseExpiry;
    private String page;
    private String profilePicture;
    private String truckingNumber;
    private int yearsOfExperience;
}