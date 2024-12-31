package com.dut.doctorcare.dto.response;

import com.dut.doctorcare.dto.request.AddressDto;
import com.dut.doctorcare.dto.request.DoctorRequest;
import com.dut.doctorcare.dto.request.SpecializationDto;
import com.dut.doctorcare.dto.request.UserBaseDto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private String id;
    private String fullName;
    private AddressDto address;
    private BigDecimal price;
    private String position;
    private String phone; //
    private String gender; //
    private LocalDate dateOfBirth; //
    private String medicalLicense; //
    private LocalDate medicalLicenseExpiry; //
    private String bio;//
    private String profilePicture;//
    private int yearsOfExperience;//
    private String emailAddress;//
    private String page;//
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalContactNumber;
    private String hospitalEmailAddress;
    private String hospitalWebsite;
    private String medicalSchool;
    private String specialPrimary;
    private LocalDate yearGraduation;
    private String truckingNumber;
}
