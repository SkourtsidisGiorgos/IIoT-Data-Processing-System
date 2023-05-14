package ntua.dblab.gskourts.streamingiot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntua.dblab.gskourts.streamingiot.model.dto.EmailDetailsDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final MailSender emailSender;

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

    public void sendEmail(EmailDetailsDTO emailDetails) {
        if (!emailEnabled) {
            return;
        }
        log.debug("Sending email with subject: {}", emailDetails.getSubject());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        if (emailCc != null && !emailCc.isEmpty())
            message.setCc(emailCc);
        if (emailBcc != null && !emailBcc.isEmpty())
            message.setBcc(emailBcc);
        message.setSubject(emailDetails.getSubject());
        message.setText(emailDetails.getMessageBody());

        log.trace("Sending email: {}", message);
        emailSender.send(message);
    }
}
