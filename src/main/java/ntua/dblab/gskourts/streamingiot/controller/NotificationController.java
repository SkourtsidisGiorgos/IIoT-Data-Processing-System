package ntua.dblab.gskourts.streamingiot.controller;

import lombok.RequiredArgsConstructor;
import ntua.dblab.gskourts.streamingiot.model.dto.EmailDetailsDTO;
import ntua.dblab.gskourts.streamingiot.service.EmailService;
import ntua.dblab.gskourts.streamingiot.util.Utils;
import org.springframework.web.bind.annotation.RestController;

import ntua.dblab.gskourts.streamingiot.model.dto.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    private final EmailService emailService;
    @PostMapping
    public ResponseEntity<Void> receiveNotification(@RequestBody Notification notification) {
        LOGGER.info("Received notification - Status: {}, Message: {}", notification.getStatus(), notification.getMessage());

        // Process the notification as needed ex. send mail or notification to teams/slack
        String message = String.format("Received notification - Status: %s, Message: %s, Timestamp: %s, Hostname: %s",
                notification.getStatus(), notification.getMessage(), System.currentTimeMillis(), Utils.getHostName());
        EmailDetailsDTO emailDetails = EmailDetailsDTO.builder()
                .subject("[notification.getStatus()] Notification from Streaming IoT")
                .messageBody("Status: " + notification.getStatus() + "\n" + message)
                .build();

        emailService.sendEmail(emailDetails);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
