package meet_eat.server.service.security;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Token;
import meet_eat.server.service.TokenService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public abstract class SecurityService<T extends Entity> {

    private final TokenService tokenService;

    protected SecurityService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public abstract boolean isLegalEntityOperation(T entity, Token authenticationToken, HttpMethod httpMethod);

    public boolean isValidAuthentication(Token authenticationToken) {
        return tokenService.isValidToken(authenticationToken);
    }

    public TokenService getTokenService() {
        return tokenService;
    }
}
