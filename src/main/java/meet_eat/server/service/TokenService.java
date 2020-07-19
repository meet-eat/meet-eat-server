package meet_eat.server.service;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

public class TokenService extends EntityService<Token, String> {

    private final UserRepository userRepository;

    @Autowired
    public TokenService(MongoRepository<Token, String> entityRepository, UserRepository userRepository) {
        super(entityRepository);
        this.userRepository = userRepository;
    }

    public Token createToken(LoginCredential loginCredential) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean isValidToken(Token token) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
