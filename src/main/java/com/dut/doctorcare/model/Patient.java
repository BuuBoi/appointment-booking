package com.dut.doctorcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "patients")
public class Patient extends BaseClazz {

//    @Id
//    @Column(name = "id")
//    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "patient")
    private List<HistoryMedical> historyMedicals;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient")
    private List<Review> reviews;
    // Enum for Gender
    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
