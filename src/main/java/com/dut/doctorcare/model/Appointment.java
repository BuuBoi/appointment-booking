package com.dut.doctorcare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "appointments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"doctor_id", "appointment_date", "appointment_time"})})
public class Appointment extends BaseClazz {

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PENDING;
    @Column(name="fee")
    private Double fee;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private String appointmentTime;

    @Column(name="address")
    private String address;


    @Column(name="appointment_created_date")
    private LocalDateTime appointmentCreatedDate;

    @Column(name="dob")
    private LocalDate dob;

    @Column(name="email")
    private String email;

    @Column(name="fullName")
    private String fullName;

    @Column(name="gender")
    private String gender;

    @Column(name="medicalDocument")
    private String medicalDocument;

    @Column(name="occupation")
    private String occupation;

    @Column(name="phone")
    private String phone;

    @Column(name="reason")
    private String reason;

    @Column(name="meeting_link")
    private String meetingLink;

    @Column(name="meeting_provider")
    private String meetingProvider;


    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum Status {
        PENDING,
        ACCEPTED,
        REJECTED
    }

}

