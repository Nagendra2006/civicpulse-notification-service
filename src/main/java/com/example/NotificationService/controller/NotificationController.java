package com.example.NotificationService.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.channel.EmailService;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendNotification(@RequestBody NotificationRequest request) {

        switch (request.getEventType()) {

            case "OTP_SEND":
                sendOtp(request);
                break;

            case "GRIEVANCE_CREATED":
                sendCreated(request);
                break;

            case "GRIEVANCE_ASSIGNED":
                sendAssigned(request);
                break;

            case "GRIEVANCE_IN_PROGRESS":
                sendInProgress(request);
                break;

            case "GRIEVANCE_RESOLVED":
                sendResolved(request);
                break;

            default:
                throw new RuntimeException("Unknown event type");
        }

        return "Notification sent";
    }

    private void sendOtp(NotificationRequest request) {

        String otp = (String) request.getData().get("otp");

        emailService.sendHtmlEmail(
                request.getRecipientEmail(),
                "OTP Verification",
                "otp", // template name (otp.html)
                Map.of("otp", otp));
    }

    private void sendCreated(NotificationRequest request) {
        String title = (String) request.getData().get("title");

        emailService.sendHtmlEmail(
                request.getRecipientEmail(),
                "Grievance Created",
                "grievance_created", // template name (grievance_created.html)
                Map.of("title", title));
    }

    private void sendAssigned(NotificationRequest request) {
        String title = (String) request.getData().get("title");
        String officer = (String) request.getData().get("officer");

        emailService.sendHtmlEmail(
                request.getRecipientEmail(),
                "Grievance Assigned",
                "grievance_assigned", // template name (grievance_assigned.html)
                Map.of("title", title, "officer", officer));
    }

    private void sendInProgress(NotificationRequest request) {
        String title = (String) request.getData().get("title");

        emailService.sendHtmlEmail(
                request.getRecipientEmail(),
                "Work Started",
                "grievance_in_progress", // template name (grievance_in_progress.html)
                Map.of("title", title));
    }

    private void sendResolved(NotificationRequest request) {

        emailService.sendHtmlEmail(
                request.getRecipientEmail(),
                "Grievance Resolved",
                "grievance-resolved",
                request.getData());
    }
}