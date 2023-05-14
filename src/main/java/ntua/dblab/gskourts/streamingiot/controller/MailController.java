package ntua.dblab.gskourts.streamingiot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntua.dblab.gskourts.streamingiot.model.dto.EmailDetailsDTO;
import ntua.dblab.gskourts.streamingiot.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/mail")
public class MailController {

    private final EmailService emailService;

    @GetMapping("/sendTestEmail")
    public void sendTestEmail() {
        log.debug("Sending test email");
        EmailDetailsDTO emailDetails = new EmailDetailsDTO().builder()
                .subject("Test email")
                .messageBody("This is a test email")
                .build();
        emailService.sendEmail(emailDetails);
    }
}
