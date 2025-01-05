package com.dut.doctorcare.dto.response;

import com.dut.doctorcare.dto.request.UserBaseDto;
import com.dut.doctorcare.model.Patient;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatientResponse  {

    private String id;
    private String email;
    private String occupation;
    private String fullName;
    private String phoneNumber;
    private String gender;
    private String avatar;
    private LocalDate dateOfBirth;
    private String address;
}
