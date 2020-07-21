package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService extends SecurityService<User> {

    @Autowired
    protected UserSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalEntityOperation(User entity, Token authenticationToken, HttpMethod httpMethod) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
