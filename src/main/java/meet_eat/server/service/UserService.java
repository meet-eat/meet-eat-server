package meet_eat.server.service;

import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends EntityService<User, String, UserRepository> {

    private static final String PASSWORD_RESET_SUBJECT = "Meet & Eat Password Reset";
    private static final String PASSWORD_RESET_TEXT_TEMPLATE = "Your new password is %s.";

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    public Optional<User> getByEmail(Email email) {
        return getRepository().findOneByEmail(email);
    }

    public void resetPassword(String emailAddress) {
        Email userEmail = new Email(emailAddress);
        Optional<User> optionalUser = getByEmail(userEmail);
        if (optionalUser.isPresent()) {
            // Generate a new password by using a password value supplier.
            PasswordValueSupplier passwordValueSupplier = new PasswordValueSupplier();
            String passwordValue = passwordValueSupplier.get();
            Password password = new Password(passwordValue);

            // Send an email with the new password to the user.
            EmailService emailService = new EmailService();
            String emailText = String.format(PASSWORD_RESET_TEXT_TEMPLATE, passwordValue);
            emailService.sendEmail(userEmail, PASSWORD_RESET_SUBJECT, emailText);

            // Write back the new password to the repository.
            User user = optionalUser.get();
            user.setPassword(password);
            put(user);
        }
    }
}
