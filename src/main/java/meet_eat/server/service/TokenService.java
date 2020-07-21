package meet_eat.server.service;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.server.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService extends EntityService<Token, String, TokenRepository> {

    private final UserService userService;

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserService userService) {
        super(tokenRepository);
        this.userService = userService;
    }

    public Token createToken(LoginCredential loginCredential) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean isValidToken(Token token) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
