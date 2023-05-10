package ntua.dblab.gskourts.streamingiot.controller;

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

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping
    public ResponseEntity<Void> receiveNotification(@RequestBody Notification notification) {
        LOGGER.info("Received notification - Status: {}, Message: {}", notification.getStatus(), notification.getMessage());

        // Process the notification as needed


        return new ResponseEntity<>(HttpStatus.OK);
    }
}
