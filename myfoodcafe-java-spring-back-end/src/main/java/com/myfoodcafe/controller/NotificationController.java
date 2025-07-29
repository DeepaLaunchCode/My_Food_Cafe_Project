package com.myfoodcafe.controller;

import com.myfoodcafe.dto.GenericResponse;
import com.myfoodcafe.entity.Notification;
import com.myfoodcafe.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Endpoints for sending and viewing notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send-sms")
    public ResponseEntity<GenericResponse> sendSms(@RequestParam String to, @RequestBody String message) {
        notificationService.sendSms(to, message);
        return ResponseEntity.ok(new GenericResponse(true, "SMS sending process initiated."));
    }

    @GetMapping
    public List<Notification> getNotifications() {
        return notificationService.getAllNotifications();
    }
}