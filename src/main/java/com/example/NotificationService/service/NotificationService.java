package com.example.NotificationService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NotificationService.channel.EmailService;
import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.entity.NotificationLog;
import com.example.NotificationService.repository.NotificationRepository;
import com.example.NotificationService.template.TemplateService;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private NotificationRepository repo;

    public String send(NotificationRequest request) {

        String message = templateService.generateMessage(
                request.getEventType(),
                request.getData());

        // try {
        //     emailService.send(
        //             request.getRecipientEmail(),
        //             request.getEventType(),
        //             message);

        //     saveLog(request, message, "SUCCESS");

            return "Notification sent";

        // } catch (Exception e) {

        //     saveLog(request, message, "FAILED");

        //     throw new RuntimeException("Notification failed");
        // }
    }

    private void saveLog(NotificationRequest req, String message, String status) {

        NotificationLog log = new NotificationLog();
        log.setEventType(req.getEventType());
        log.setRecipient(req.getRecipientEmail());
        log.setMessage(message);
        log.setStatus(status);
        log.setCreatedAt(LocalDateTime.now());

        repo.save(log);
    }
}