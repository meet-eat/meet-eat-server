package meet_eat.server.service.security;

import meet_eat.data.entity.Token;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class TokenSecurityService extends SecurityService<Token> {

    @Autowired
    protected TokenSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalPost(Token entity, Token authenticationToken) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalPut(Token entity, Token authenticationToken) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalDelete(Token entity, Token authenticationToken) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
