package ntua.dblab.gskourts.streamingiot.service;

import lombok.extern.slf4j.Slf4j;
import ntua.dblab.gskourts.streamingiot.model.dto.EmailDetailsDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${application.email.enabled}")
    private boolean emailEnabled;

    @Value("${application.email.from}")
    private String emailFrom;

    @Value("${application.email.to}")
    private String emailTo;

    @Value("${application.email.cc}")
    private String emailCc;
    @Value("${application.email.bcc}")
    private String emailBcc;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(EmailDetailsDTO emailDetails) {
        if (!emailEnabled) {
            return;
        }
        log.debug("Sending email with subject: {}", emailDetails.getSubject());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        message.setCc(emailCc);
        message.setBcc(emailBcc);
        message.setSubject(emailDetails.getSubject());
        message.setText(emailDetails.getMessageBody());

        emailSender.send(message);
    }
}
