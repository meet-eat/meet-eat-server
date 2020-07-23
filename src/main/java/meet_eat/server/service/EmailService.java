package meet_eat.server.service;

import meet_eat.data.entity.user.Email;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Email SENDER = new Email("unknown@meet-eat.com");
    //private final JavaMailSender emailSender;

    public void sendEmail(Email recipient, String subject, String text) {
        /*SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER.toString());
        message.setTo(recipient.toString());
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);*/

        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
