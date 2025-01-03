package com.dut.doctorcare.service.impl;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {
    public String getDoctorNotificationTemplate(String patientName, String date, String time) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        .email-container {
                            font-family: Arial, sans-serif;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                            background-color: #f8f9fa;
                        }
                        .header {
                            background-color: #007bff;
                            color: white;
                            padding: 20px;
                            text-align: center;
                            border-radius: 5px;
                        }
                        .content {
                            background-color: white;
                            padding: 20px;
                            margin-top: 20px;
                            border-radius: 5px;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        }
                        .appointment-details {
                            margin: 20px 0;
                            padding: 15px;
                            background-color: #f8f9fa;
                            border-left: 4px solid #007bff;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
                            <h2>New Appointment Request</h2>
                        </div>
                        <div class="content">
                            <p>Dear Doctor,</p>
                            <p>You have received a new appointment request from your patient.</p>
                
                            <div class="appointment-details">
                                <p><strong>Patient Name:</strong> %s</p>
                                <p><strong>Date:</strong> %s</p>
                                <p><strong>Time:</strong> %s</p>
                            </div>
                
                            <p>Please review and respond to this appointment request through the system.</p>
                            <p>Best regards,<br>Medical Center</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(patientName, date, time);
    }

    public String getPatientNotificationTemplate(String doctorName, String date,
                                                 String time, boolean isAccepted) {
        String status = isAccepted ? "accepted" : "rejected";
        String statusColor = isAccepted ? "#28a745" : "#dc3545";

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        .email-container {
                            font-family: Arial, sans-serif;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                            background-color: #f8f9fa;
                        }
                        .header {
                            background-color: %s;
                            color: white;
                            padding: 20px;
                            text-align: center;
                            border-radius: 5px;
                        }
                        .content {
                            background-color: white;
                            padding: 20px;
                            margin-top: 20px;
                            border-radius: 5px;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        }
                        .appointment-details {
                            margin: 20px 0;
                            padding: 15px;
                            background-color: #f8f9fa;
                            border-left: 4px solid %s;
                        }
                        .status-badge {
                            display: inline-block;
                            padding: 8px 15px;
                            background-color: %s;
                            color: white;
                            border-radius: 20px;
                            font-weight: bold;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
                            <h2>Appointment Update</h2>
                        </div>
                        <div class="content">
                            <p>Dear Patient,</p>
                            <p>Your appointment request has been <span class="status-badge">%s</span></p>
                
                            <div class="appointment-details">
                                <p><strong>Doctor:</strong> Dr. %s</p>
                                <p><strong>Date:</strong> %s</p>
                                <p><strong>Time:</strong> %s</p>
                            </div>
                
                            %s
                
                            <p>Best regards,<br>Medical Center</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                statusColor,
                statusColor,
                statusColor,
                status,
                doctorName,
                date,
                time,
                isAccepted ?
                        "<p>We look forward to seeing you at your appointment. Please arrive 10 minutes early to complete any necessary paperwork.</p>" :
                        "<p>We apologize for any inconvenience. Please feel free to schedule another appointment at your convenience.</p>"
        );
    }
}
