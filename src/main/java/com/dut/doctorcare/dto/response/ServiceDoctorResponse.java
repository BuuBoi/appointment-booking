package com.dut.doctorcare.dto.response;

import com.dut.doctorcare.dto.request.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDoctorResponse {
    private String id;
    private String fullName;
    private String profilePicture;
    private boolean isActive;
    private AddressDto address;
    private int yearsOfExperience;
    private String specialization; // Just the name
    private String hospitalName;
}