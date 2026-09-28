package com.example.NotificationService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.NotificationService.entity.NotificationLog;

public interface NotificationRepository extends JpaRepository<NotificationLog, Long> {
}