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
    public boolean isLegalGet(Token authenticationToken) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalPost(User entity, Token authenticationToken) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalPut(User entity, Token authenticationToken) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalDelete(User entity, Token authenticationToken) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
