package com.dut.doctorcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "doctors")
public class Doctor extends BaseClazz {

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToMany
    @JoinTable(
            name = "doctor_symptom", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "doctor_id"), // Khóa ngoại đến bảng Doctor
            inverseJoinColumns = @JoinColumn(name = "symptom_id") // Khóa ngoại đến bảng Symptom
    )
    private List<Symptom> symptoms = new ArrayList<Symptom>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor")
    private List<HistoryMedical> historyMedicals;

    @OneToMany(mappedBy = "doctor")
    private List<Review> reviews;

    @OneToMany(mappedBy = "doctor")
    private List<WeeklyAvailable> weeklyAvailables;

    @Column(name = "full_name", nullable = false)
    private String fullName;



    private BigDecimal price;
    private String position;
    @Column(name = "phone_number")
    private String phone; //

    @Column(name="gender")
    private String gender; //

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth; //

    @Column(name = "medical_license")
    private String medicalLicense; //

    @Column(name = "medical_license_expiry")
    private LocalDate medicalLicenseExpiry; //

    @Column(name = "bio")
    private String bio;//

    @Column(name = "profile_picture")
    private String profilePicture;//

    @Column(name = "years_of_experience")
    private int yearsOfExperience;//

    @Column(name = "email_address")
    private String emailAddress;//

    @Column(name = "page")
    private String page;//

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "hospital_address")
    private String hospitalAddress;

    @Column(name = "hospital_contact_number")
    private String hospitalContactNumber;

    @Column(name = "hospital_email_address")
    private String hospitalEmailAddress;

    @Column(name = "hospital_website")
    private String hospitalWebsite;

    @Column(name = "medical_school")
    private String medicalSchool;

    @Column(name = "special_primary")
    private String specialPrimary;

    @Column(name = "special_secondary")
    private LocalDate yearGraduation;

    @Column(name = "trucking_number")
    private String truckingNumber;



}
