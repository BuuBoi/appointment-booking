package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.model.Appointment;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationService {
    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailTemplateService templateService;

    public void sendDoctorAppointmentNotification(Appointment appointment) throws MessagingException {
        String doctorEmail = appointment.getDoctor().getEmailAddress();
        String patientName = appointment.getFullName();
        String date = appointment.getAppointmentDate().toString();
        String time = appointment.getAppointmentTime().toString();

        emailService.sendHtmlEmail(
                doctorEmail,
                "New Appointment Request",
                templateService.getDoctorNotificationTemplate(patientName, date, time)
        );
    }

    public void sendPatientStatusNotification(Appointment appointment) throws MessagingException {
        String patientEmail = appointment.getUser().getEmail();
        String doctorName = appointment.getDoctor().getFullName();
        String date = appointment.getAppointmentDate().toString();
        String time = appointment.getAppointmentTime().toString();
        boolean isAccepted = appointment.getStatus() == Appointment.Status.ACCEPTED;

        emailService.sendHtmlEmail(
                patientEmail,
                isAccepted ? "Appointment Confirmed" : "Appointment Update",
                templateService.getPatientNotificationTemplate(doctorName, date, time, isAccepted)
        );
    }
}
