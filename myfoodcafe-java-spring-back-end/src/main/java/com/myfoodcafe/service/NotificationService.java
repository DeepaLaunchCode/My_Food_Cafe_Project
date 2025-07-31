package com.myfoodcafe.service;

import com.myfoodcafe.entity.Notification;
import com.myfoodcafe.repository.NotificationRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String to, String body) {
        // Add country code if missing (assuming US numbers for this example)
        String recipientNumber = to.startsWith("+") ? to : "+1" + to;

        Notification notification = new Notification();
        notification.setRecipientPhoneNumber(recipientNumber);
        notification.setMessage(body);
        notification.setSentAt(LocalDateTime.now());

        try {
            Message message = Message.creator(
                    new PhoneNumber(recipientNumber),
                    new PhoneNumber(fromPhoneNumber),
                    body
            ).create();

            System.out.println("SMS sent successfully! SID: " + message.getSid());
            notification.setStatus("SUCCESS");
            notification.setTwilioMessageId(message.getSid());

        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
            notification.setStatus("FAILED");
        }
        notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}