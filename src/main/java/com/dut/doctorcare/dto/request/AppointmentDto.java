package com.dut.doctorcare.dto.request;

import com.dut.doctorcare.model.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class AppointmentDto {

        private String id;
//        private Appointment.Status status = Appointment.Status.PENDING;
        @JsonProperty("status") // dam bao dung ten status khi nhan request
        private Appointment.Status status = Appointment.Status.PENDING;
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
        private String meetingLink;

    }

