package com.dut.doctorcare.dto.request;

import lombok.*;

import java.time.LocalDate;
//@Data is a convenient shortcut annotation that bundles the features of @ToString, @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor together
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseDto {
    private String id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String gender;

    private LocalDate dateOfBirth;

    private AddressDto addressDto;
}
