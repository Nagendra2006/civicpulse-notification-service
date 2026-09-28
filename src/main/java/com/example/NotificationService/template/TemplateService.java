package com.example.NotificationService.template;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class TemplateService {

    public String generateMessage(String eventType, Map<String, Object> data) {

        return switch (eventType) {

            case "GRIEVANCE_CREATED" ->
                "Your grievance '" + data.get("title") + "' has been created.";

            case "GRIEVANCE_ASSIGNED" ->
                "You have been assigned grievance: " + data.get("title") +
                        " (Priority: " + data.get("priority") + ")";

            case "GRIEVANCE_RESOLVED" ->
                "Your grievance '" + data.get("title") + "' has been resolved.";

            case "GRIEVANCE_REOPENED" ->
                "Your grievance '" + data.get("title") + "' has been reopened.";

            default -> "Notification received.";
        };
    }
}