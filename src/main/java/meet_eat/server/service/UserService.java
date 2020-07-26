package meet_eat.server.service;

import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.UserRepository;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService extends EntityService<User, String, UserRepository> {

    private static final String PASSWORD_RESET_SUBJECT = "Meet & Eat Password Reset";
    private static final String PASSWORD_RESET_TEXT_TEMPLATE = "Your new password is %s.";
    private static final int PASSWORD_BASIC_CHAR_COUNT = 25;
    private static final int PASSWORD_SPECIAL_CHAR_COUNT = 2;
    private static final int PASSWORD_DIGIT_COUNT = 5;

    private final OfferService offerService;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, OfferService offerService, TokenService tokenService) {
        super(userRepository);
        this.offerService = offerService;
        this.tokenService = tokenService;
    }

    public Optional<User> getByEmail(Email email) {
        return getRepository().findOneByEmail(Objects.requireNonNull(email));
    }

    public void resetPassword(String emailAddress) {
        Email userEmail = new Email(emailAddress);
        Optional<User> optionalUser = getByEmail(userEmail);
        if (optionalUser.isPresent()) {
            // Generate a new password by using a password value supplier.
            PasswordValueSupplier passwordValueSupplier = new PasswordValueSupplier(PASSWORD_BASIC_CHAR_COUNT,
                    PASSWORD_SPECIAL_CHAR_COUNT, PASSWORD_DIGIT_COUNT);
            String passwordValue = passwordValueSupplier.get();
            Password password = Password.createHashedPassword(passwordValue);

            // Send an email with the new password to the user.
            EmailService emailService = new EmailService();
            String emailText = String.format(PASSWORD_RESET_TEXT_TEMPLATE, passwordValue);
            emailService.sendEmail(userEmail, PASSWORD_RESET_SUBJECT, emailText);

            // Write back the new password to the repository.
            User user = optionalUser.get();
            user.setPassword(password.derive(user.getIdentifier(), SecurityService.PASSWORD_ITERATION_COUNT));
            put(user);
        }
    }

    @Override
    public User post(User entity) {
        Password derivedPassword = entity.getPassword().derive(entity.getIdentifier(), SecurityService.PASSWORD_ITERATION_COUNT);
        entity.setPassword(derivedPassword);
        return super.post(entity);
    }

    @Override
    public User put(User entity) {
        if (hasModifiedPassword(entity)) {
            Password derivedPassword = entity.getPassword().derive(entity.getIdentifier(), SecurityService.PASSWORD_ITERATION_COUNT);
            entity.setPassword(derivedPassword);
        }
        return super.put(entity);
    }

    @Override
    public void delete(User entity) {
        Objects.requireNonNull(entity);
        offerService.deleteByCreator(entity);
        tokenService.deleteByUser(entity);
        super.delete(entity);
    }

    @Override
    public void delete(String identifier) {
        Objects.requireNonNull(identifier);
        offerService.deleteByCreator(identifier);
        tokenService.deleteByUser(identifier);
        super.delete(identifier);
    }

    @Override
    public boolean existsPostConflict(User entity) {
        return existsEmailConflict(entity) || super.existsPostConflict(entity);
    }

    @Override
    public boolean existsPutConflict(User entity) {
        return existsEmailConflict(entity);
    }

    private boolean existsEmailConflict(User user) {
        Optional<User> optionalUserByEmail = getByEmail(user.getEmail());
        return optionalUserByEmail.isPresent()
                && !optionalUserByEmail.get().getIdentifier().equals(user.getIdentifier());
    }

    private boolean hasModifiedPassword(User user) {
        Optional<User> optionalPersistentUser = getRepository().findById(user.getIdentifier());
        if (optionalPersistentUser.isPresent()) {
            User persistentUser = optionalPersistentUser.get();
            return !persistentUser.getPassword().equals(user.getPassword());
        }
        return false;
    }
}
