package meet_eat.server.service;

import meet_eat.data.entity.user.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(Email sender, Email recipient, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender.toString());
        message.setTo(recipient.toString());
        message.setSubject(Objects.requireNonNull(subject));
        message.setText(Objects.requireNonNull(text));
        emailSender.send(message);
    }
}
