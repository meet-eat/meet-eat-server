package meet_eat.server.service;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class TokenService extends EntityService<Token, String, TokenRepository> {

    private static final String ERROR_MESSAGE_INVALID_LOGIN_CREDENTIALS = "Given login credentials must be valid.";
    private static final int SALT_BYTE_LENGTH = 16;

    private final UserService userService;

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserService userService) {
        super(tokenRepository);
        this.userService = userService;
    }

    public Token createToken(LoginCredential loginCredential) {
        // Check whether the user exists and login credentials are valid.
        Objects.requireNonNull(loginCredential);
        Optional<User> optionalUser = userService.getByEmail(loginCredential.getEmail());
        if (optionalUser.isEmpty() || !isValidLoginCredential(loginCredential)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_LOGIN_CREDENTIALS);
        }

        // Generate a random salt for the token hash value
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        random.nextBytes(salt);

        // Concat the salt's bytes, email and current time to get a string with "high entropy", randomness respectively.
        StringBuilder stringBuilder = new StringBuilder();
        for (byte elem : salt) {
            stringBuilder.append(elem);
        }
        stringBuilder.append(loginCredential.getEmail());
        stringBuilder.append(LocalDateTime.now());

        // Hash the generated string, create the token and insert it into the repository.
        String tokenValue = Hashing.sha256().hashString(stringBuilder, Charsets.UTF_16).toString();
        return getRepository().insert(new Token(optionalUser.get(), tokenValue));
    }

    public boolean isValidLoginCredential(LoginCredential loginCredential) {
        if (Objects.isNull(loginCredential)) {
            return false;
        }
        Optional<User> optionalUser = userService.getByEmail(loginCredential.getEmail());
        return optionalUser.isPresent() && optionalUser.get().getPassword().equals(loginCredential.getPassword());
    }

    public boolean isValidToken(Token token) {
        if (Objects.isNull(token)) {
            return false;
        }
        Optional<Token> repoToken = getRepository().findById(token.getIdentifier());
        return repoToken.isPresent() && token.equals(repoToken.get());
    }

    public void deleteByUser(User user) {
        getRepository().deleteByUser(Objects.requireNonNull(user));
    }

    public void deleteByUser(String userId) {
        Optional<User> optionalUser = userService.get(userId);
        if (optionalUser.isPresent()) {
            deleteByUser(optionalUser.get());
        }
    }
}
